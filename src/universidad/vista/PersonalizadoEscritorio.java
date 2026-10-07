/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package universidad.vista;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JDesktopPane;

/**
 *
 * @author UPrO
 */
public class PersonalizadoEscritorio extends JDesktopPane{
    private BufferedImage img;

    public PersonalizadoEscritorio() {
        try {
            img = ImageIO.read(getClass().getResourceAsStream("/imagen/ULP.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("NO encontro la imagen!");
        }
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        //muestra la imagen en el JDesktoPane
        g.drawImage(img, 50, 50, this);
    }
   
}
