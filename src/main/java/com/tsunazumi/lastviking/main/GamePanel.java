package com.tsunazumi.lastviking.main;

import com.tsunazumi.lastviking.inputs.KeyboardInputs;
import com.tsunazumi.lastviking.inputs.MouseInputs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static com.tsunazumi.lastviking.utils.Constants.PlayerConstants.*;
import static com.tsunazumi.lastviking.utils.Constants.Directions.*;

public class GamePanel extends JPanel {

  private MouseInputs mouseInputs;
  private float xDelta = 100, yDelta = 100;
  private BufferedImage img;
  private BufferedImage[][] animations;
  private int aniTick, aniIndex, aniSpeed = 15;
  private int playerAction = RUNNING;
  private int playerDir = -1;
  private boolean moving = false;

  public GamePanel() {
    mouseInputs = new MouseInputs(this);
    importImage();
    loadAnimations();
    setPanelSize();
    addKeyListener(new KeyboardInputs(this));
    addMouseListener(mouseInputs);
    addMouseMotionListener(mouseInputs);
  }

  private void loadAnimations() {
    animations = new BufferedImage[9][6];
    for (int j = 0; j < animations.length; j++) {
      for (int i = 0; i < animations[j].length; i++) {
        animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
      }
    }
  }

  private void importImage() {
    try (InputStream is = getClass().getResourceAsStream("/player_sprites.png")) {
      img = ImageIO.read(is);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void setPanelSize() {
    Dimension size = new Dimension(1280, 800);
    setMinimumSize(size);
    setPreferredSize(size);
    setMaximumSize(size);
  }

  public void setDirection(int direction) {
    this.playerDir = direction;
    moving = true;
  }

  public void setMoving(boolean moving) {
    this.moving = moving;
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    updateAnimationTick();
    setAnimation();
    updatePos();

    g.drawImage(
        animations[playerAction][aniIndex],
        (int)xDelta, (int)yDelta, 256, 160, null);


  }

  private void updatePos() {
    if (moving) {
      switch (playerDir) {
        case LEFT:
          xDelta -= 5;
          break;
        case UP:
          yDelta -= 5;
          break;
        case RIGHT:
          xDelta += 5;
          break;
        case DOWN:
          yDelta += 5;
          break;
      }
    }
  }

  private void setAnimation() {
    if (moving) {
      playerAction = RUNNING;
    } else {
      playerAction = IDLE;
    }
  }

  private void updateAnimationTick() {
    aniTick++;
    if (aniTick >= aniSpeed) {
      aniTick = 0;
      aniIndex++;
      if (aniIndex >= getSpriteAmount(playerAction)) {
        aniIndex = 0;
      }
    }
  }

}