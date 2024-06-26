package com.ridanisaurus.emendatusenigmatica.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ToolAction;
import net.neoforged.neoforge.common.ToolActions;

import javax.annotation.Nullable;

public class PaxelItem extends DiggerItem
{
    private static final ToolAction PAXEL_DIG = ToolAction.get("paxel_dig");

    public PaxelItem(Tier tier, TagKey<Block> blocks, Properties properties) {
        super(tier, blocks, properties);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return toolAction == PAXEL_DIG || ToolActions.DEFAULT_AXE_ACTIONS.contains(toolAction)
                || ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction) || ToolActions.DEFAULT_SHOVEL_ACTIONS.contains(toolAction);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos blockPos = ctx.getClickedPos();
        Player player = ctx.getPlayer();
        BlockState blockState = level.getBlockState(blockPos);
        BlockState resultToSet = useAsAxe(blockState, ctx);

        if(resultToSet != null) {
            if(ctx.getClickedFace() == Direction.DOWN) {
                return InteractionResult.PASS;
            }

            BlockState foundResult = blockState.getToolModifiedState(ctx, ToolActions.SHOVEL_FLATTEN, false);

            if(foundResult != null && level.isEmptyBlock(blockPos.above())) {
                level.playSound(player, blockPos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                resultToSet = foundResult;
            } else if(blockState.getBlock() instanceof CampfireBlock && blockState.getValue(CampfireBlock.LIT)) {
                if(!level.isClientSide) {
                    level.levelEvent(null, LevelEvent.SOUND_EXTINGUISH_FIRE, blockPos, 0);
                }
                CampfireBlock.dowse(player, level, blockPos, blockState);
                resultToSet = blockState.setValue(CampfireBlock.LIT, false);
            }
            if(resultToSet == null) {
                return InteractionResult.PASS;
            }
        }

        if(!level.isClientSide) {
            ItemStack stack = ctx.getItemInHand();
            if(player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, stack);
            }
            level.setBlock(blockPos, resultToSet, Block.UPDATE_ALL_IMMEDIATE);
            if(player != null) {
                EquipmentSlot slot = stack.getEquipmentSlot();
                if(slot != null) {
                    stack.hurtAndBreak(1, player, slot);
                }
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Nullable
    private BlockState useAsAxe(BlockState state, UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos blockpos = ctx.getClickedPos();
        Player player = ctx.getPlayer();
        BlockState resultToSet = state.getToolModifiedState(ctx, ToolActions.AXE_SCRAPE, false);

        if(resultToSet != null) {
            level.playSound(player, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return resultToSet;
        }

        resultToSet = state.getToolModifiedState(ctx, ToolActions.AXE_SCRAPE, false);

        if(resultToSet != null) {
            level.playSound(player, blockpos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.levelEvent(player, LevelEvent.PARTICLES_SCRAPE, blockpos, 0);
            return resultToSet;
        }

        resultToSet = state.getToolModifiedState(ctx, ToolActions.AXE_WAX_OFF, false);

        if(resultToSet != null) {
            level.playSound(player, blockpos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.levelEvent(player, LevelEvent.PARTICLES_WAX_OFF, blockpos, 0);
            return resultToSet;
        }

        return null;
    }
}
