package com.unascribed.lanthanoid.item.eldritch.tool;

import java.util.List;

import com.google.common.base.Strings;
import com.unascribed.lanthanoid.Lanthanoid;
import com.unascribed.lanthanoid.glyph.IGlyphHolderItem;
import com.unascribed.lanthanoid.item.GlyphItemHelper;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemEldritchShears extends ItemShears implements IGlyphHolderItem {

	private IIcon glyphs;
	
	public ItemEldritchShears() {
		super();
		setCreativeTab(Lanthanoid.inst.creativeTabEquipment);
		setTextureName("lanthanoid:eldritch_shears");
		setUnlocalizedName("eldritch_shears");
	}
	
	@Override
	public int getMaxMilliglyphs(ItemStack stack) {
		return 500_000;
	}
	
	@Override
	public float func_150893_a(ItemStack stack, Block block) {
		return getMilliglyphs(stack) > 0 ? super.func_150893_a(stack, block) : super.func_150893_a(stack, block)/3;
	}
	
	@Override
	public float getDigSpeed(ItemStack stack, Block block, int meta) {
		return func_150893_a(stack, block);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
		GlyphItemHelper.doAddInformation(this, stack, player, list, advanced);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean equipped) {
		GlyphItemHelper.doUpdate(this, stack, world, entity, slot, equipped);
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase ent) {
		return GlyphItemHelper.doBlockDestroyed(this, stack, world, block, x, y, z, ent);
	}
	
	@Override
	public String getUnlocalizedNameInefficiently(ItemStack p_77657_1_) {
		return Strings.nullToEmpty(getUnlocalizedName(p_77657_1_));
	}
	
	@Override
	public void registerIcons(IIconRegister register) {
		super.registerIcons(register);
		glyphs = register.registerIcon("lanthanoid:eldritch_glyph_dig");
	}
	
	@Override
	public IIcon getGlyphs(ItemStack is) {
		return glyphs;
	}
	
	@Override
	public float getGlyphColorRed(ItemStack is) {
		return GlyphItemHelper.getDefaultGlyphColorRed(this, is);
	}
	
	@Override
	public float getGlyphColorGreen(ItemStack is) {
		return GlyphItemHelper.getDefaultGlyphColorGreen(this, is);
	}
	
	@Override
	public float getGlyphColorBlue(ItemStack is) {
		return GlyphItemHelper.getDefaultGlyphColorBlue(this, is);
	}
	
	@Override
	public float getGlyphColorAlpha(ItemStack is) {
		return GlyphItemHelper.getDefaultGlyphColorAlpha(this, is);
	}
	
}
