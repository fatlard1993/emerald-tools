#!/usr/bin/env python3
"""Generate Emerald Tools' mod menu icon: four of the tools, framed the way the armour is.

Emerald Armor's icon is a two by two of green pieces on brown, and these two are a pair, so
this is the same grid with tools in it. The brown is what makes them legible: an emerald tool
on an emerald ground is one green shape, which is what the old grey icon was avoiding and what
the page's icon avoided by showing the armour instead.

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
    """A texture this mod ships, read from the repo rather than the jar."""
    with open(os.path.join(HERE, path), "rb") as f:
        return decode_png(f.read())


def sample(pixels, width, height):
    """Nearest neighbour to any size, up or down. Pixel art is never smoothed."""
    src_h, src_w = len(pixels), len(pixels[0])
    return [[pixels[y * src_h // height][x * src_w // width] for x in range(width)]
            for y in range(height)]


def fill(colour, size=16):
    return [[colour] * size for _ in range(size)]


GREEN = (19, 155, 55, 255)     # Emerald Armor's ground
BROWN = (96, 53, 31, 255)      # and its frames, which the tools have to read against
CELL = 7
ITEMS = "src/main/resources/assets/emerald-tools-justfatlard/textures/item/%s.png"
QUARTERS = (("emerald_pickaxe", 0, 0), ("emerald_axe", 9, 0),
            ("emerald_sword", 0, 9), ("emerald_shovel", 9, 9))


def build_icon():
    sprite = fill(GREEN)
    for name, left, top in QUARTERS:
        for y in range(top, top + CELL):
            for x in range(left, left + CELL):
                sprite[y][x] = BROWN
        stamp(sprite, sample(here(ITEMS % name), CELL, CELL), left, top)
    return scale(sprite, 8)


if __name__ == "__main__":
    icon = build_icon()
    assert len(icon) == 128 and len(icon[0]) == 128, "mod menu icons are 128x128"
    write_png(OUT, icon)
