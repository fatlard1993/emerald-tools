#!/usr/bin/env python3
"""Generate Emerald Tools' mod menu icon: all five tools, at full size, in frames.

Emerald Armor's icon is framed items on green and these two are a pair, so this is the same
ground with the tools on it - three above, two below, the way Quartz Tools arranges its five.
The frames are what make them legible: an emerald tool on an emerald ground is one green
shape, and the brown behind each one is what the head reads against.

Drawn at 128x128 rather than at 16 and scaled, because five sprites at their own size do not
fit a sixteen pixel square and shrinking them is what made the previous attempt unreadable.

Pure stdlib PNG reader and writer (zlib + struct) so it runs without Pillow. No vanilla art is
used, so no Minecraft jar is needed. Deterministic: re-running produces identical bytes.

Usage: python3 generate_icon.py
"""

import os
import struct
import zlib

HERE = os.path.dirname(os.path.abspath(__file__))
OUT = os.path.join(HERE, "src/main/resources/assets/emerald-tools-justfatlard/icon.png")

CLEAR = (0, 0, 0, 0)


def decode_png(data):
    """Minimal PNG reader: no interlacing, every colour type and bit depth
    vanilla actually ships. Returns rows of RGBA tuples."""
    pos = 8
    idat = b""
    width = height = depth = ctype = None
    palette = trns = None
    while pos < len(data):
        (length,) = struct.unpack(">I", data[pos:pos + 4])
        tag = data[pos + 4:pos + 8]
        body = data[pos + 8:pos + 8 + length]
        pos += 12 + length
        if tag == b"IHDR":
            width, height, depth, ctype, _, _, interlace = struct.unpack(">IIBBBBB", body)
            assert interlace == 0, "interlaced PNG not supported"
        elif tag == b"PLTE":
            palette = body
        elif tag == b"tRNS":
            trns = body
        elif tag == b"IDAT":
            idat += body
        elif tag == b"IEND":
            break

    channels = {0: 1, 2: 3, 3: 1, 4: 2, 6: 4}[ctype]
    stride = (width * channels * depth + 7) // 8
    step = max(1, (channels * depth) // 8)
    raw = zlib.decompress(idat)
    out = bytearray(stride * height)
    prev = bytearray(stride)
    p = 0
    for y in range(height):
        filt = raw[p]
        p += 1
        line = bytearray(raw[p:p + stride])
        p += stride
        if filt == 1:
            for i in range(step, stride):
                line[i] = (line[i] + line[i - step]) & 0xFF
        elif filt == 2:
            for i in range(stride):
                line[i] = (line[i] + prev[i]) & 0xFF
        elif filt == 3:
            for i in range(stride):
                a = line[i - step] if i >= step else 0
                line[i] = (line[i] + ((a + prev[i]) >> 1)) & 0xFF
        elif filt == 4:
            for i in range(stride):
                a = line[i - step] if i >= step else 0
                b = prev[i]
                c = prev[i - step] if i >= step else 0
                pa, pb, pc = abs(b - c), abs(a - c), abs(a + b - 2 * c)
                pr = a if (pa <= pb and pa <= pc) else (b if pb <= pc else c)
                line[i] = (line[i] + pr) & 0xFF
        out[y * stride:(y + 1) * stride] = line
        prev = line

    pixels = []
    if depth < 8:
        per = 8 // depth
        mask = (1 << depth) - 1
        for y in range(height):
            base = y * stride
            row = []
            for x in range(width):
                i = x * channels
                value = (out[base + i // per] >> (8 - depth * (i % per + 1))) & mask
                if ctype == 3:
                    r, g, b = palette[value * 3:value * 3 + 3]
                    a = trns[value] if trns and value < len(trns) else 255
                    row.append((r, g, b, a))
                else:
                    v = value * 255 // mask
                    row.append((v, v, v, 255))
            pixels.append(row)
        return pixels

    for y in range(height):
        base = y * stride
        row = []
        for x in range(width):
            i = base + x * channels
            if ctype == 6:
                row.append(tuple(out[i:i + 4]))
            elif ctype == 2:
                row.append((out[i], out[i + 1], out[i + 2], 255))
            elif ctype == 4:
                row.append((out[i], out[i], out[i], out[i + 1]))
            elif ctype == 0:
                row.append((out[i], out[i], out[i], 255))
            else:
                r, g, b = palette[out[i] * 3:out[i] * 3 + 3]
                a = trns[out[i]] if trns and out[i] < len(trns) else 255
                row.append((r, g, b, a))
        pixels.append(row)
    return pixels


def write_png(path, pixels):
    """pixels: rows of RGBA tuples."""
    height = len(pixels)
    width = len(pixels[0])
    raw = b"".join(b"\x00" + b"".join(bytes(px) for px in row) for row in pixels)

    def chunk(tag, body):
        c = tag + body
        return struct.pack(">I", len(body)) + c + struct.pack(">I", zlib.crc32(c))

    ihdr = struct.pack(">IIBBBBB", width, height, 8, 6, 0, 0, 0)
    png = (b"\x89PNG\r\n\x1a\n" + chunk(b"IHDR", ihdr)
           + chunk(b"IDAT", zlib.compress(raw, 9)) + chunk(b"IEND", b""))
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "wb") as f:
        f.write(png)
    print("wrote %s (%dx%d)" % (path, width, height))


def scale(pixels, n):
    """Nearest neighbour only: these are pixel textures, never smooth them."""
    return [[px for px in row for _ in range(n)] for row in pixels for _ in range(n)]


def blank(size=16):
    return [[CLEAR] * size for _ in range(size)]


def stamp(sprite, art, left, top):
    """Lay art onto the sprite at (left, top); transparent source pixels leave
    the sprite alone."""
    for y, row in enumerate(art):
        for x, px in enumerate(row):
            if px[3] and 0 <= top + y < len(sprite) and 0 <= left + x < len(sprite[0]):
                sprite[top + y][left + x] = px
    return sprite


def crop(pixels, left, top, width, height):
    return [row[left:left + width] for row in pixels[top:top + height]]



def here(path):
    """A texture this mod ships, read from the repo."""
    with open(os.path.join(HERE, path), "rb") as f:
        return decode_png(f.read())


def sample(pixels, width, height):
    """Nearest neighbour to any size. Pixel art is never smoothed."""
    src_h, src_w = len(pixels), len(pixels[0])
    return [[pixels[y * src_h // height][x * src_w // width] for x in range(width)]
            for y in range(height)]


def rect(sprite, colour, left, top, width, height):
    for y in range(top, top + height):
        for x in range(left, left + width):
            sprite[y][x] = colour


SIZE = 128
GREEN = (19, 155, 55, 255)     # Emerald Armor's ground and its darker edge
EDGE = (23, 120, 33, 255)
BORDER = (90, 50, 31, 255)     # and its frames
INNER = (96, 53, 31, 255)
MARGIN = 4                     # the green showing round the outside
FRAME, PAD = 36, 2             # a frame, and the wood around the item in it
ITEMS = "src/main/resources/assets/emerald-tools-justfatlard/textures/item/%s.png"
ROWS = ((("emerald_pickaxe", "emerald_axe", "emerald_sword"), 22, (4, 46, 88)),
        (("emerald_shovel", "emerald_hoe"), 70, (25, 67)))


def build_icon():
    sprite = [[EDGE] * SIZE for _ in range(SIZE)]
    rect(sprite, GREEN, MARGIN, MARGIN, SIZE - 2 * MARGIN, SIZE - 2 * MARGIN)
    inner = FRAME - 2 * PAD
    for names, top, lefts in ROWS:
        for name, left in zip(names, lefts):
            rect(sprite, BORDER, left, top, FRAME, FRAME)
            rect(sprite, INNER, left + PAD, top + PAD, inner, inner)
            stamp(sprite, sample(here(ITEMS % name), inner, inner), left + PAD, top + PAD)
    return sprite


if __name__ == "__main__":
    icon = build_icon()
    assert len(icon) == SIZE and len(icon[0]) == SIZE, "mod menu icons are 128x128"
    write_png(OUT, icon)
