package justfatlard.emerald_tools;

import java.util.Set;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Very occasionally, the emerald hoe turns up an emerald in the soil.
 *
 * <p>The tool is made of the thing, so having it find the thing costs nothing
 * to explain and gives a hoe a reason to be carried by somebody who already has
 * a better one. It is deliberately rare enough that nobody breaks ground for
 * it: it is a thing that happens to you while farming, not a way to farm.
 *
 * <p><b>One roll per swing, not per block.</b> With Useful Hoe installed a
 * single click can break a hundred and sixty blocks of ground, and a per-block
 * roll would turn an ornament into an emerald farm that scales with the Reach
 * enchantment. Per action, the rate is the same whether the player is tilling
 * one square or a field.
 *
 * <p>Registered in a phase ordered ahead of {@link Event#DEFAULT_PHASE} and it
 * always returns {@link InteractionResult#PASS}: it observes, it never handles.
 * Useful Hoe returns SUCCESS from its own listener when it takes an area
 * action, which would swallow anything registered after it, and mod load order
 * is not something to rely on.
 */
public final class EmeraldSoil {
	private EmeraldSoil() {}

	private static final Identifier PHASE =
		Identifier.fromNamespaceAndPath(EmeraldTools.MOD_ID, "emerald_soil");

	/** One in this many swings. Rare enough to be a surprise, not a yield. */
	private static final int ONE_IN = 1500;

	/** Ground that a hoe turns into a field. Coarse and rooted dirt only crumble, so they are not it. */
	private static final Set<Block> BREAKS_INTO_FIELD =
		Set.of(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.DIRT_PATH);

	public static void register() {
		UseBlockCallback.EVENT.addPhaseOrdering(PHASE, Event.DEFAULT_PHASE);
		UseBlockCallback.EVENT.register(PHASE, EmeraldSoil::onUseBlock);
	}

	private static InteractionResult onUseBlock(Player player, Level world, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide() || hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
		if (!player.getItemInHand(hand).is(EmeraldTools.EMERALD_HOE)) return InteractionResult.PASS;

		BlockPos pos = hit.getBlockPos();
		BlockState state = world.getBlockState(pos);
		if (!BREAKS_INTO_FIELD.contains(state.getBlock())) return InteractionResult.PASS;

		// The same thing tilling itself requires: room above. Checking it here
		// keeps the roll off swings that were never going to break ground.
		if (!world.getBlockState(pos.above()).canBeReplaced()) return InteractionResult.PASS;

		if (world.getRandom().nextInt(ONE_IN) != 0) return InteractionResult.PASS;

		Block.popResource(world, pos, new ItemStack(Items.EMERALD));
		// A 1-in-1500 event needs saying out loud or it lands in the grass unseen.
		world.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.7F, 1.2F);

		return InteractionResult.PASS;
	}
}
