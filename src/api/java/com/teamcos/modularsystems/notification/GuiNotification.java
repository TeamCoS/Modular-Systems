package com.teamcos.modularsystems.notification;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiNotification extends Gui {
    private static final ResourceLocation backGround = new ResourceLocation("textures/gui/achievement/achievement_background.png");
    private Minecraft minecraft;
    private int width;
    private int height;
    private String title;
    private String description;
    private Notification notification;
    private long timeOpen;
    private RenderItem itemRenderer;
    private boolean hide;

    private List<Notification> notifications = new ArrayList<Notification>();

    public GuiNotification(Minecraft mc)
    {
        this.minecraft = mc;
        this.itemRenderer = new RenderItem();
    }

    public void queueNotification(Notification notification1)
    {
        if(notifications.isEmpty()) {
            this.title = notification1.getTitle();
            this.description = notification1.getDescription();
            this.timeOpen = Minecraft.getSystemTime();
            this.notification = notification1;
        }
        notifications.add(notification1);
    }

    public void moveToNextNotification()
    {
        if(!notifications.isEmpty()) {
            notifications.remove(0);
            setDead();
            if(!notifications.isEmpty()) {
                this.title = notifications.get(0).getTitle();
                this.description = notifications.get(0).getDescription();
                this.timeOpen = Minecraft.getSystemTime();
                this.notification = notifications.get(0);
                this.hide = false;
            }
        }
    }

    private void updateScale()
    {
        GL11.glViewport(0, 0, this.minecraft.displayWidth, this.minecraft.displayHeight);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        this.width = this.minecraft.displayWidth;
        this.height = this.minecraft.displayHeight;
        ScaledResolution scaledresolution = new ScaledResolution(this.minecraft, this.minecraft.displayWidth, this.minecraft.displayHeight);
        this.width = scaledresolution.getScaledWidth();
        this.height = scaledresolution.getScaledHeight();
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, (double)this.width, (double)this.height, 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
    }

    public void update()
    {
        if (this.notification != null && this.timeOpen != 0L && Minecraft.getMinecraft().thePlayer != null)
        {
            double d0 = (double)(Minecraft.getSystemTime() - this.timeOpen) / 3000.0D;

            if (!this.hide)
            {
                if (d0 < 0.0D || d0 > 1.0D)
                {
                    this.timeOpen = 0L;
                    moveToNextNotification();
                    return;
                }
            }
            else if (d0 > 0.5D)
            {
                d0 = 0.5D;
            }

            this.updateScale();
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            double d1 = d0 * 2.0D;

            if (d1 > 1.0D)
            {
                d1 = 2.0D - d1;
            }

            d1 *= 4.0D;
            d1 = 1.0D - d1;

            if (d1 < 0.0D)
            {
                d1 = 0.0D;
            }

            d1 *= d1;
            d1 *= d1;
            int i = (this.width / 2) - 80;
            int j = 0 - (int)(d1 * 36.0D);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            this.minecraft.getTextureManager().bindTexture(backGround);
            GL11.glDisable(GL11.GL_LIGHTING);
            this.drawTexturedModalRect(i, j, 96, 202, 160, 32);

            if (this.hide)
            {
                this.minecraft.fontRenderer.drawSplitString(this.description, i + 30, j + 7, 120, -1);
            }
            else
            {
                this.minecraft.fontRenderer.drawString(this.title, i + 30, j + 7, -256);
                this.minecraft.fontRenderer.drawString(this.description, i + 30, j + 18, -1);
            }

            RenderHelper.enableGUIStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glEnable(GL11.GL_LIGHTING);
            this.itemRenderer.renderItemAndEffectIntoGUI(this.minecraft.fontRenderer, this.minecraft.getTextureManager(), this.notification.getIcon(), i + 8, j + 8);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
    }

    public void setDead()
    {
        this.notification = null;
        this.timeOpen = 0L;
    }
}
