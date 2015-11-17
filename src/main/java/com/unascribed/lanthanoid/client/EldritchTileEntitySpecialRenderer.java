package com.unascribed.lanthanoid.client;

import org.lwjgl.opengl.GL11;

import com.unascribed.lanthanoid.Lanthanoid;
import com.unascribed.lanthanoid.init.LBlocks;
import com.unascribed.lanthanoid.tile.TileEntityEldritch;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.EntityEnchantmentTableParticleFX;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import scala.collection.parallel.ParIterableLike.Min;

public class EldritchTileEntitySpecialRenderer extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity teraw, double xdouble, double ydouble, double zdouble, float partialTicks) {
		float x = (float)xdouble;
		float y = (float)ydouble;
		float z = (float)zdouble;
		IIcon glyphs = null;
		switch (teraw.getBlockMetadata()) {
			case 3:
				glyphs = LBlocks.machine.collectorGlyphs;
				break;
			case 4:
				glyphs = LBlocks.machine.distributorGlyphs;
				break;
			case 5:
				glyphs = LBlocks.machine.chargerGlyphs;
				break;
		}
		if (glyphs == null || !(teraw instanceof TileEntityEldritch)) {
			if (teraw.hasWorldObj()) {
				Lanthanoid.log.error("WRONG TILE ENTITY AT {}, {}, {} IN {} (Normally, this would have crashed your game. Maybe log spam is worse. Who knows.)", teraw.xCoord, teraw.yCoord, teraw.zCoord, teraw.getWorldObj().provider.getDimensionName());
			}
			// if there's no world object, it's not worth complaining.
			// we're stuck in some crappy fakeworld and it's not getting better.
			return;
		}
		TileEntityEldritch te = (TileEntityEldritch)teraw;
		
		float playerAnim = te.playerAnim;
		if (te.playersNearby) {
			if (playerAnim < 20) {
				playerAnim += partialTicks;
			}
		} else {
			if (playerAnim > partialTicks) {
				playerAnim -= partialTicks;
			}
		}
		playerAnim /= 20;
		
		float glyphCount = Math.max(1, Math.min(te.milliglyphs, 100000))/100000f;
		
		float q = Math.max(0.25f, playerAnim);
		float q2 = Math.max(0.25f, playerAnim*glyphCount);
		
		
		GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDepthMask(true);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			
			/*if (te.getBlockMetadata() == 5) {
				GL11.glPushMatrix();
					GL11.glColor3f(0, 1, 0);
					GL11.glTranslatef(x, y+1.05f, z+1);
					GL11.glRotatef(90f, -1, 0, 0);
					drawExtrudedIcon(LBlocks.machine.coil, 0.05f);
				GL11.glPopMatrix();
			}*/
			
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
			
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
			
			float r = q-(playerAnim*glyphCount);
			float g = q-playerAnim;
			float b = q2;
			
			float t = (te.ticksExisted+partialTicks)+((te.xCoord*31)+(te.yCoord*31)+(te.zCoord*31));
			GL11.glPushMatrix();
				GL11.glTranslatef(x, y+0.75f, z);
				for (int i = 0; i < 4; i++) {
					GL11.glRotatef(-90f, 0, 1, 0);
					GL11.glTranslatef(0, 0, -1f);
					
					t += 67;
					
					float sin = MathHelper.sin(t/20);
					
					GL11.glPushMatrix();
						GL11.glRotatef(180f, 0, 1, 0);
						GL11.glTranslatef(-0.5f, 0, (0.09f*playerAnim)+0.01f);
						GL11.glRotatef(((sin*10)-15)*playerAnim, 1, 0, 0);
						GL11.glRotatef((sin*4)*playerAnim, 0, 0, 1);
						GL11.glTranslatef(-0.5f, -0.25f, 0);
						GL11.glScalef(1, 0.5f, 1);
						GL11.glColor4f(r, g, b, 0.15f);
						drawExtrudedHalfIcon(glyphs, 0.5f);
						GL11.glTranslatef(0, 0, 0.05f);
						GL11.glColor4f(r, g, b, 0.5f);
						drawExtrudedHalfIcon(glyphs, 0.05f);
						/*tessellator.startDrawingQuads();
						tessellator.setColorOpaque_F(q, q-playerAnim, q);
						tessellator.addVertexWithUV(-0.5, -0.25, 0, minU, maxV);
						tessellator.addVertexWithUV(0.5, -0.25, 0, maxU, maxV);
						tessellator.addVertexWithUV(0.5, 0.25, 0, maxU, minV);
						tessellator.addVertexWithUV(-0.5, 0.25, 0, minU, minV);
						tessellator.draw();*/
					GL11.glPopMatrix();
					
				}
			GL11.glPopMatrix();
			
			float sin = (float)(Math.sin(t/20)+1)/4;
			String str = (te.milliglyphs/1000)+"."+((te.milliglyphs%1000)/10);
			FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
			
			GL11.glPushMatrix();
				GL11.glTranslatef((float)x+0.5f, (float)y+1.25f+sin, (float)z+0.5f);
				GL11.glRotatef(t*4, 0, 1, 0);
				GL11.glScalef(0.025f, -0.025f, 0.025f);
				fr.drawString(str, -fr.getStringWidth(str)/2, 0, -1);
				
				GL11.glRotatef(180, 0, 1, 0);
				fr.drawString(str, -fr.getStringWidth(str)/2, 0, -1);
			GL11.glPopMatrix();
			
			String str2 = "[debug]";
			GL11.glPushMatrix();
				GL11.glTranslatef((float)x+0.5f, (float)y+1.4f+sin, (float)z+0.5f);
				GL11.glRotatef(t*4, 0, 1, 0);
				GL11.glScalef(0.0125f, -0.0125f, 0.0125f);
				fr.drawString(str2, -fr.getStringWidth(str2)/2, 0, -1);
				
				GL11.glRotatef(180, 0, 1, 0);
				fr.drawString(str2, -fr.getStringWidth(str2)/2, 0, -1);
			GL11.glPopMatrix();
				
			GL11.glDepthMask(false);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	private void drawExtrudedHalfIcon(IIcon icon, float thickness) {
		if (icon == null) return;
		ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMinU(), icon.getMinV(), icon.getMaxU(), icon.getMinV()+((icon.getMaxV()-icon.getMinV())/2), icon.getIconWidth(), icon.getIconHeight()/2, thickness);
	}
	
	private void drawExtrudedIcon(IIcon icon, float thickness) {
		if (icon == null) return;
		ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMinU(), icon.getMinV(), icon.getMaxU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), thickness);
	}

}
