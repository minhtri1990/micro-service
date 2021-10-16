package com.its.module.utils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;
import com.its.module.utils.Constants.*;

public class CaptchaUtils {
    private static Random random = new Random();
    public static String getRandomCaptchaCode() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<Captcha.LENGTH; i++) {
            stringBuilder.append(Captcha.CHARS[random.nextInt(Captcha.CHARS.length)]);
        }
        return stringBuilder.toString();
    }

    public static BufferedImage getCaptchaImage(String captchaString, int width, int height) {
        try {
            Random random = new Random();
            Color backgroundColor = Color.WHITE;
            Color borderColor = new Color(217, 221, 229);
            Color textColor = Color.black;
            //Color circleColor = new Color(190, 160, 150);
            Font textFont = new Font("Verdana", Font.BOLD, 20);
            int charsToPrint = 6;
            int circlesToDraw = 5;
            float horizMargin = 10.0f;
            double rotationRange = 0;
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
            g.setColor(backgroundColor);
            g.fillRect(0, 0, width, height);
            // lets make some noisey circles
            //g.setColor(circleColor);
            for (int i = 0; i < circlesToDraw; i++) {
                int L = (int) (Math.random() * height / 2.0);
                int X = (int) (Math.random() * width - L);
                int Y = (int) (Math.random() * height - L);
                g.draw3DRect(X, Y, L, L, true);
            }
            g.setColor(textColor);
            g.setFont(textFont);
            FontMetrics fontMetrics = g.getFontMetrics();
            int maxAdvance = fontMetrics.getMaxAdvance();
            int fontHeight = fontMetrics.getHeight();
            // i removed 1 and l and i because there are confusing to users...
            // Z, z, and N also get confusing when rotated
            // this should ideally be done for every language...
            // 0, O and o removed because there are confusing to users...
            // i like controlling the characters though because it helps prevent confusion
            float spaceForLetters = -horizMargin * 2 + width;
            float spacePerChar = spaceForLetters / (charsToPrint - 1.0f);

            for (int i = 0; i < captchaString.length(); i++) {
                char characterToShow = captchaString.charAt(i);
                // this is a separate canvas used for the character so that
                // we can rotate it independently
                int charWidth = fontMetrics.charWidth(characterToShow);
                fontMetrics.charWidth(charWidth + 20);
                int charDim = Math.max(maxAdvance, fontHeight);
                int halfCharDim = (int) (charDim / 2);
                BufferedImage charImage = new BufferedImage(charDim, charDim, BufferedImage.TYPE_INT_ARGB);
                Graphics2D charGraphics = charImage.createGraphics();
                charGraphics.translate(halfCharDim, halfCharDim);
                double angle = rotationRange;
                charGraphics.transform(AffineTransform.getRotateInstance(angle));
                charGraphics.translate(-halfCharDim, -halfCharDim);
                charGraphics.setColor(new Color(random.nextInt(150), random.nextInt(150), random.nextInt(100)));
                charGraphics.setFont(textFont);
                int charX = (int) (0.5 * charDim - 0.5 * charWidth);
                charGraphics.drawString("" + characterToShow, charX, (int) ((charDim - fontMetrics.getAscent()) / 2 + fontMetrics.getAscent()));
                float x = horizMargin + spacePerChar * (i) - charDim / 2.0f;
                int y = (int) ((height - charDim) / 2);
                g.drawImage(charImage, (int) x, y, charDim, charDim, null, null);
                charGraphics.dispose();
            }
            g.setColor(borderColor);
            g.drawRoundRect(0, 0, width - 1, height - 1, 10, 10);
            g.dispose();
            return bufferedImage;
        } catch (Exception ioe) {
            throw new RuntimeException("Unable to build image", ioe);
        }
    }
}
