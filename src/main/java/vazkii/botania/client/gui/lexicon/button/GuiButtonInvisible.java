/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 14, 2014, 8:34:01 PM (GMT)]
 */
package vazkii.botania.client.gui.lexicon.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.FontHelper;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.botania.common.lib.LibMisc;

public class GuiButtonInvisible extends GuiButtonLexicon {

	GuiLexicon gui;
	public ItemStack displayStack = null;
	float timeHover = 0;
	
	public GuiButtonInvisible(GuiLexicon gui, int par1, int par2, int par3, int par4, int par5, String par6Str) {
		super(par1, par2, par3, par4, par5, par6Str);
		this.gui = gui;
	}

	
	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
		field_146123_n = par2 >= xPosition && par3 >= yPosition && par2 < xPosition + width && par3 < yPosition + height;
		int k = getHoverState(field_146123_n);
		boolean showStack = displayStack != null && !displayString.isEmpty();

		if(!displayString.isEmpty() && k == 2)
			timeHover = Math.min(5, timeHover + gui.timeDelta);
		else timeHover = Math.max(0, timeHover - gui.timeDelta);

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		int color = 0;
		String format = FontHelper.getFormatFromString(displayString);
		if(format.length() > 1) {
			char key = format.charAt(format.length() - 1);
			if(key == 'o' && format.length() > 3)
				key = format.charAt(1);
			
			for(EnumChatFormatting ecf : EnumChatFormatting.class.getEnumConstants())
				if(ecf.getFormattingCode() == key) {
					if(ecf.ordinal() > 15)
						ecf = EnumChatFormatting.BLACK;
					color = LibMisc.CONTROL_CODE_COLORS[ecf.ordinal()]; 
					break;
				}
		}
		
		int maxalpha = 0x22;
		int alpha = Math.min(maxalpha, (int) (timeHover / 4 * maxalpha));
		drawRect(xPosition - 5, yPosition, (int) (xPosition - 5 + timeHover * 24), yPosition + height, alpha << 24 | color);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		
		boolean unicode = par1Minecraft.fontRenderer.getUnicodeFlag();
		par1Minecraft.fontRenderer.setUnicodeFlag(true);
		par1Minecraft.fontRenderer.drawString(displayString, xPosition + (showStack ? 7 : 0), yPosition + (height - 8) / 2, 0);
		par1Minecraft.fontRenderer.setUnicodeFlag(unicode);
		
		if(showStack) {
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			RenderHelper.enableGUIStandardItemLighting();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			RenderItem.getInstance().renderItemIntoGUI(par1Minecraft.fontRenderer, par1Minecraft.renderEngine, displayStack, xPosition * 2 - 6, yPosition * 2 + 4);
			RenderHelper.disableStandardItemLighting();
			GL11.glEnable(GL11.GL_BLEND);
		}
		GL11.glPopMatrix();
	}
	
}
