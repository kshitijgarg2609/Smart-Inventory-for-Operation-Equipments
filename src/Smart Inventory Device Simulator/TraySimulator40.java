import java.awt.*;
import javax.swing.*;
class TraySimulator40
{
Dimension dim;
static int w,h;
int width=800,height=600;
JFrame jf;
JPanel toggle_button_panel;
JToggleButton equip[][] = new JToggleButton[5][8];
TraySimulator40()
{
UIManager.put("ToggleButton.select", Color.green);
dim=(Toolkit.getDefaultToolkit()).getScreenSize();
w=(int)(dim.getWidth());
h=(int)(dim.getHeight());
jf=new JFrame("Smart Inventory Device Simulator");
jf.setBounds(((w-width)/2),((h-height)/2),width,height);
jf.setResizable(false);
jf.getContentPane().setLayout(null);
addComponents();
jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE);
jf.setVisible(true);
jf.repaint();
jf.revalidate();
}
void addComponents()
{
toggle_button_panel = new JPanel();
toggle_button_panel.setBounds(10, 11, 760, 535);
toggle_button_panel.setLayout(new GridLayout(5, 8, 10, 10));
toggle_button_panel.setBorder(BorderFactory.createLineBorder(Color.orange,5));
jf.getContentPane().add(toggle_button_panel);
addEquipments();
}
void addEquipments()
{
for(int i=0;i<5;i++)
{
for(int j=0;j<8;j++)
{
equip[i][j]=new JToggleButton((char)('A'+i)+""+j,true);
equip[i][j].setBackground(Color.magenta);
toggle_button_panel.add(equip[i][j]);
}
}
}
}