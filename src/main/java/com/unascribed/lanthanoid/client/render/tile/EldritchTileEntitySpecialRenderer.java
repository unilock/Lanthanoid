package com.unascribed.lanthanoid.client.render.tile;

import java.util.Collection;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.unascribed.lanthanoid.Lanthanoid;
import com.unascribed.lanthanoid.client.ModelSimpleBook;
import com.unascribed.lanthanoid.client.Rendering;
import com.unascribed.lanthanoid.glyph.IGlyphHolder;
import com.unascribed.lanthanoid.init.LBlocks;
import com.unascribed.lanthanoid.tile.TileEntityEldritch;
import com.unascribed.lanthanoid.tile.TileEntityEldritchDistributor;
import com.unascribed.lanthanoid.tile.TileEntityEldritchFaithPlate;
import com.unascribed.lanthanoid.tile.TileEntityEldritchInductor;
import com.unascribed.lanthanoid.tile.TileEntityEldritchWithBooks;
import com.unascribed.lanthanoid.util.LVec3;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.chunk.Chunk;

public class EldritchTileEntitySpecialRenderer extends TileEntitySpecialRenderer {

	private static ModelSimpleBook book = new ModelSimpleBook();
	
	private static final ResourceLocation bookTex = new ResourceLocation("textures/entity/enchanting_table_book.png");
	
	private static RenderItem ri = new RenderItem() {
		@Override
		public boolean shouldBob() { return false; }
		@Override
		public boolean shouldSpreadItems() { return false; }
	};
	private static EntityItem dummy = new EntityItem(null);
	
	public static void renderEldritchBlock(float x, float y, float z, float partialTicks,
			float playerAnim, float glyphCount, int bookCount, IIcon glyphs, int brightness, float t, int hash,
			boolean inventory, boolean blink, boolean nearby) {
		float q = Math.max(0.25f, playerAnim);
		float q2 = Math.max(0.25f, playerAnim*glyphCount);
		
		
		GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_LIGHTING);
			
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDepthMask(true);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			
			int j = brightness % 65536;
			int k = brightness / 65536;
			
			float oldX = OpenGlHelper.lastBrightnessX;
			float oldY = OpenGlHelper.lastBrightnessY;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j+((240f-j)*playerAnim), k+((240f-k)*playerAnim));
			
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
			
			float r = q2;
			float g = q-playerAnim;
			float b = q-(playerAnim*glyphCount);
			float a = 0.5f;
			
			if (glyphCount >= 0.99999f) {
				a = 1;
			}
			if (blink) {
				if (glyphCount <= 0.01f) {
					r = 1;
					g = 1;
					b = 1;
				} else {
					r = ((MathHelper.sin(t/((hash%5)+3))+1)/4)+0.5f;
					g = glyphCount*r;
					b = 0;
				}
			}
			
			GL11.glPushMatrix();
				GL11.glTranslatef(x, y+0.75f, z);
				for (int i = 0; i < (inventory ? 2 : 4); i++) {
					GL11.glRotatef(-90f, 0, 1, 0);
					GL11.glTranslatef(0, 0, -1f);
					
					t += 67;
					
					float sin = MathHelper.sin(t/20);
					
					GL11.glPushMatrix();
						GL11.glRotatef(180f, 0, 1, 0);
						GL11.glTranslatef(-0.5f, 0, (0.09f*playerAnim)+0.01f);
						if (!blink) {
							GL11.glRotatef(((sin*10)-15)*playerAnim, 1, 0, 0);
							GL11.glRotatef((sin*4)*playerAnim, 0, 0, 1);
						} else {
							GL11.glRotatef(-15*playerAnim, 1, 0, 0);
						}
						GL11.glTranslatef(-0.5f, -0.25f, 0);
						GL11.glScalef(1, 0.5f, 1);
						GL11.glColor4f(r, g, b, a/3);
						boolean fancy = Minecraft.isFancyGraphicsEnabled() && nearby;
						if (fancy && !inventory) {
							GL11.glDepthMask(false);
							Rendering.drawExtrudedHalfIcon(glyphs, 0.5f);
							GL11.glDepthMask(true);
						}
						GL11.glTranslatef(0, 0, 0.05f);
						GL11.glColor4f(r, g, b, a);
						if (fancy) {
							Rendering.drawExtrudedHalfIcon(glyphs, 0.05f);
						} else {
							GL11.glDisable(GL11.GL_CULL_FACE);
							Tessellator tessellator = Tessellator.instance;
							tessellator.startDrawingQuads();
							tessellator.addVertexWithUV(0 + 0, 0 + 1, ri.zLevel, glyphs.getMinU(), glyphs.getMinV());
							tessellator.addVertexWithUV(0 + 1, 0 + 1, ri.zLevel, glyphs.getMaxU(), glyphs.getMinV());
							tessellator.addVertexWithUV(0 + 1, 0 + -1, ri.zLevel, glyphs.getMaxU(), glyphs.getMaxV());
							tessellator.addVertexWithUV(0 + 0, 0 + -1, ri.zLevel, glyphs.getMinU(), glyphs.getMaxV());
							tessellator.draw();
							GL11.glEnable(GL11.GL_CULL_FACE);
						}
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
			
			if (nearby) {
				GL11.glColor3f(1, 1, 1);
				GL11.glDisable(GL11.GL_BLEND);
				
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
				boolean moveX = false;
				int moveY = 0;
				for (int i = 0; i < bookCount; i++) {
					GL11.glPushMatrix();
						Minecraft.getMinecraft().renderEngine.bindTexture(bookTex);
						GL11.glTranslatef(x+0.0625f+(moveX ? 0.5f : 0f)+(moveY*0.05f), y+1.07f+(moveY*0.13f), z+0.5f);
						GL11.glRotatef(90f, 1, 0, 0);
						GL11.glRotatef((((i*67)^hash)%60)-30, 0, 0, 1);
						book.render(null, 0, 0, 1, 0, 0.0F, 0.0625F);
					GL11.glPopMatrix();
					if (!moveX) {
						moveX = true;
					} else {
						moveX = false;
						moveY++;
					}
				}
				GL11.glDepthMask(false);
				GL11.glEnable(GL11.GL_LIGHTING);
			}
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, oldX, oldY);
		GL11.glPopMatrix();
		GL11.glDepthMask(true);
	}
	
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
			case 6:
				glyphs = LBlocks.machine.faithPlateGlyphs;
				break;
			case 7:
				glyphs = LBlocks.machine.collectorGlyphs;
				break;
			case 8:
				glyphs = LBlocks.machine.boostPadGlyphs;
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
		
		float glyphCount = Math.max(1, Math.min(te.getMilliglyphs(), te.getMaxMilliglyphs()))/(float)te.getMaxMilliglyphs();
		int brightness = te.getWorldObj().getLightBrightnessForSkyBlocks(te.xCoord, te.yCoord+1, te.zCoord, 0);
		float t = (te.ticksExisted+partialTicks)+((te.xCoord*31)+(te.yCoord*31)+(te.zCoord*31));
		
		int books = 0;
		
		if (te instanceof TileEntityEldritchWithBooks) {
			books = ((TileEntityEldritchWithBooks) te).getBookCount();
		}
		
		renderEldritchBlock(x, y, z, partialTicks, playerAnim, glyphCount, books, glyphs,
				brightness, t,
				te.hashCode(), false, te instanceof TileEntityEldritchDistributor && ((TileEntityEldritchDistributor)te).drain,
				Minecraft.getMinecraft().thePlayer.getDistanceSq(te.xCoord+0.5, te.yCoord+0.5, te.zCoord+0.5) < (64*64));
		
		if (Minecraft.getMinecraft().gameSettings.showDebugInfo && Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode) {
			FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
			
			GL11.glPushMatrix();
				List<String> li = te.getDebugText();
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glTranslatef(x+0.5f, y+1.25f, z+0.25f);
				GL11.glRotatef(45, 1, 0, 0);
				for (String str : li) {
					GL11.glPushMatrix();
						GL11.glRotatef(180, 0, 1, 0);
						GL11.glScalef(0.0125f, -0.0125f, 0.0125f);
						fr.drawString(str, -fr.getStringWidth(str)/2, 0, 0xFFFFFFFF);
					GL11.glPopMatrix();
					GL11.glTranslatef(0, -0.12f, 0);
				}
			GL11.glPopMatrix();
			if (te instanceof TileEntityEldritchDistributor) {
				GL11.glPushMatrix();
				GL11.glTranslatef(x+0.5f, y+0.5f, z+0.5f);
				
				float baseX = te.xCoord+0.5f;
				float baseY = te.yCoord+0.5f;
				float baseZ = te.zCoord+0.5f;
				
				int minX = te.xCoord-12;
				int minZ = te.zCoord-12;
				int maxX = te.xCoord+12;
				int maxZ = te.zCoord+12;
				int minY = te.yCoord-12;
				int maxY = te.yCoord+12;
				
				int minCX = (te.xCoord/16)-1;
				int minCZ = (te.zCoord/16)-1;
				int maxCX = (te.xCoord/16)+1;
				int maxCZ = (te.zCoord/16)+1;
				
				TileEntity max = null;
				IGlyphHolder maxHolder = null;
				int maxDiff = 0;
				TileEntity min = null;
				IGlyphHolder minHolder = null;
				int minDiff = 0;
				
				GL11.glLineWidth(2);
				GL11.glDepthMask(false);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_LINE_SMOOTH);
				GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
				GL11.glBegin(GL11.GL_LINES);
				for (int cX = minCX; cX <= maxCX; cX++) {
					for (int cZ = minCZ; cZ <= maxCZ; cZ++) {
						Chunk c = te.getWorldObj().getChunkFromChunkCoords(cX, cZ);
						for (TileEntity cur : (Collection<TileEntity>)c.chunkTileEntityMap.values()) {
							if (cur.xCoord >= minX && cur.xCoord <= maxX
									&& cur.zCoord >= minZ && cur.zCoord <= maxZ
									&& cur.yCoord >= minY && cur.yCoord <= maxY) {
								if (cur instanceof IGlyphHolder && cur != te) {
									IGlyphHolder holder = (IGlyphHolder) cur;
									LVec3 dir = new LVec3(cur.xCoord - te.xCoord, cur.yCoord - te.yCoord, cur.zCoord - te.zCoord);
									dir.normalize();
									double sX = te.xCoord+0.5+dir.xCoord;
									double sY = te.yCoord+0.5+dir.yCoord;
									double sZ = te.zCoord+0.5+dir.zCoord;
									double eX = cur.xCoord+0.5;
									double eY = cur.yCoord+0.5;
									double eZ = cur.zCoord+0.5;
									eX += te.getWorldObj().rand.nextGaussian()*0.15;
									eY += te.getWorldObj().rand.nextGaussian()*0.15;
									eZ += te.getWorldObj().rand.nextGaussian()*0.15;
									MovingObjectPosition mop = te.getWorldObj().rayTraceBlocks(
											Vec3.createVectorHelper(sX, sY, sZ),
											Vec3.createVectorHelper(eX, eY, eZ));
									if (mop != null) {
										if (mop.typeOfHit == MovingObjectType.BLOCK && te.getWorldObj().getTileEntity(mop.blockX, mop.blockY, mop.blockZ) == holder) {
											GL11.glColor4f(1, 1, 1, 0.15f);
											GL11.glVertex3d(0, 0, 0);
											GL11.glVertex3d((cur.xCoord+0.5f)-baseX, (cur.yCoord+0.5f)-baseY, (cur.zCoord+0.5f)-baseZ);
											if (((TileEntityEldritchDistributor) te).drain) {
												if (holder.canReceiveGlyphs()) {
													if (minHolder == null || holder.getMilliglyphs() < minHolder.getMilliglyphs() && holder.getMilliglyphs() < holder.getMaxMilliglyphs()) {
														min = cur;
														minHolder = holder;
														minDiff = Integer.MAX_VALUE;
													}
												}
											} else {
												if (holder.getMilliglyphs() > te.getMilliglyphs() && holder.canSendGlyphs()
														&& holder.getMilliglyphs() > 0 && te.getMilliglyphs() < te.getMaxMilliglyphs()) {
													if (maxHolder == null || holder.getMilliglyphs() > maxHolder.getMilliglyphs()) {
														max = cur;
														maxHolder = holder;
														maxDiff = holder.getMilliglyphs()-te.getMilliglyphs();
													}
												} else if (holder.getMilliglyphs() < te.getMilliglyphs() && holder.canReceiveGlyphs() && holder.getMilliglyphs() < holder.getMaxMilliglyphs()) {
													if (minHolder == null || holder.getMilliglyphs() < minHolder.getMilliglyphs()) {
														min = cur;
														minHolder = holder;
														minDiff = te.getMilliglyphs()-holder.getMilliglyphs();
													}
												}
											}
										} else {
											GL11.glColor4f(1, 0, 0, 0.5f);
											GL11.glVertex3d(0, 0, 0);
											GL11.glVertex3d(mop.hitVec.xCoord-baseX, mop.hitVec.yCoord-baseY, mop.hitVec.zCoord-baseZ);
										}
									}
								}
							}
						}
					}
				}
				GL11.glEnd();
				if (min != null && minDiff > 0) {
					if (minDiff != Integer.MAX_VALUE) {
						GL11.glLineWidth(minDiff/10000f);
					} else {
						GL11.glLineWidth(2);
					}
					GL11.glBegin(GL11.GL_LINES);
						GL11.glColor3f(0, 1, 0);
						GL11.glVertex3d(0, 0, 0);
						GL11.glVertex3d((min.xCoord+0.5f)-baseX, (min.yCoord+0.5f)-baseY, (min.zCoord+0.5f)-baseZ);
					GL11.glEnd();
				}
				if (max != null && maxDiff > 0) {
					if (maxDiff != Integer.MAX_VALUE) {
						GL11.glLineWidth(maxDiff/10000f);
					} else {
						GL11.glLineWidth(2);
					}
					GL11.glBegin(GL11.GL_LINES);
						GL11.glColor3f(0, 1, 1);
						GL11.glVertex3d(0, 0, 0);
						GL11.glVertex3d((max.xCoord+0.5f)-baseX, (max.yCoord+0.5f)-baseY, (max.zCoord+0.5f)-baseZ);
					GL11.glEnd();
				}
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glPopMatrix();
			}
			GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING);
		}
		
		if (teraw instanceof TileEntityEldritchFaithPlate) {
			TileEntityEldritchFaithPlate tefp = (TileEntityEldritchFaithPlate) teraw;
			int len = tefp.animHeight;
			GL11.glPushMatrix();
				Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
				int j = brightness % 65536;
				int k = brightness / 65536;
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
				Tessellator tess = Tessellator.instance;
				RenderBlocks rb = RenderBlocks.getInstance();
				rb.blockAccess = te.getWorldObj();
				float q = (10*len);
				float ofs = (Math.abs(MathHelper.sin((Math.min(q, tefp.bounceAnimTicks+(6*len)+partialTicks)/q)*(float)Math.PI))/2)*len;
				GL11.glTranslatef(x-te.xCoord, y+ofs-te.yCoord+0.025f, z-te.zCoord);
				GL11.glDisable(GL11.GL_LIGHTING);
				tess.startDrawingQuads();
				rb.setOverrideBlockTexture(LBlocks.machine.getIcon(1, 6));
				rb.setRenderBounds(6/16f, (15/16f)-ofs, 6/16f, 10/16f, 15/16f, 10/16f);
				rb.lockBlockBounds = true;
				rb.renderBlockAllFaces(LBlocks.machine, te.xCoord, te.yCoord, te.zCoord);
				rb.lockBlockBounds = false;
				rb.setRenderBounds(0.01, 0.9375, 0.01, 0.99, 0.99, 0.99);
				rb.lockBlockBounds = true;
				rb.renderBlockAllFaces(LBlocks.machine, te.xCoord, te.yCoord, te.zCoord);
				rb.lockBlockBounds = false;
				tess.draw();
			GL11.glPopMatrix();
		} else if (teraw instanceof TileEntityEldritchInductor) {
			GL11.glPushMatrix();
				Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
				GL11.glColor3f(0, 1, 0);
				GL11.glTranslatef(x, y+1, z+1);
				GL11.glRotatef(90f, -1, 0, 0);
				Rendering.drawExtrudedIcon(LBlocks.machine.coil, 1/32f);
			GL11.glPopMatrix();
			for (int i = 0; i < 4; i++) {
				ItemStack stack = ((TileEntityEldritchInductor)te).getStackInSlot(i);
				if (stack != null) {
					float iX = (i % 2 == 0 ? 0.75f : 0.25f);
					float iZ = (i > 1 ? 0.25f : 0.75f);
					iZ -= 0.25f;
					GL11.glPushMatrix();
						ri.setRenderManager(RenderManager.instance);
						GL11.glTranslatef(x+iX, y+(33/32f), z+iZ);
						GL11.glRotatef(90f, 1, 0, 0);
						/*float ofsX = 0;
						float ofsY = 0.125f;
						GL11.glTranslatef(ofsX, ofsY, 0);*/
						
						/*GL11.glDisable(GL11.GL_TEXTURE_2D);
						GL11.glColor3f(1, 1, 1);
						GL11.glLineWidth(2.5f);
						GL11.glBegin(GL11.GL_LINES);
							GL11.glVertex3f(0, 0, 0);
							GL11.glVertex3f(0, 0, -0.5f);
						GL11.glEnd();
						GL11.glEnable(GL11.GL_TEXTURE_2D);*/
						
						//GL11.glRotatef(((te.hashCode()^(i*3433))/67f)%180, 0, 0, 1);
						//GL11.glTranslatef(-ofsX, -ofsY, 0);
						try {
							dummy.setWorld(te.getWorldObj());
							dummy.setEntityItemStack(stack);
							dummy.ticksExisted = 0;
							dummy.age = 0;
							dummy.hoverStart = 0;
							ri.doRender(dummy, 0, 0, 0, 0f, 0f);
						} catch (Throwable tr) {;
							tr.printStackTrace();
						}
					GL11.glPopMatrix();
				}
			}
		}
	}

}
