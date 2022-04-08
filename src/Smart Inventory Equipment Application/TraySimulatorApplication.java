import java.awt.*;
import javax.swing.*;
class TraySimulatorApplication
{
Dimension dim;
static int w,h;
int width=800,height=600;
JFrame jf;
JPanel button_status_panel;
JButton ip_scan,connect,disconnect;
JComboBox<String> scanned_ip;
DefaultComboBoxModel<String> dtm = new DefaultComboBoxModel<>();
JLabel ind[] = new JLabel[40];
JLabel cnt_display;
int cnt=-1;
String cnt_prefix="Number of Equipments Placed : ";
boolean iflg[] = new boolean[40];
TraySimulatorApplication()
{
dim=(Toolkit.getDefaultToolkit()).getScreenSize();
w=(int)(dim.getWidth());
h=(int)(dim.getHeight());
jf=new JFrame("Smart Inventory Equipment Tray Application");
jf.setBounds(((w-width)/2),((h-height)/2),width,height);
jf.setResizable(false);
jf.getContentPane().setLayout(null);
addComponents();
jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
jf.setVisible(true);
jf.repaint();
jf.revalidate();
}
void addComponents()
{
button_status_panel = new JPanel();
button_status_panel.setBounds(10, 63, 760, 435);
button_status_panel.setLayout(new GridLayout(5, 8, 10, 10));
button_status_panel.setBorder(BorderFactory.createLineBorder(Color.cyan,5));
jf.getContentPane().add(button_status_panel);
ip_scan = new JButton("Scan Device");
ip_scan.setBounds(28, 29, 127, 23);
jf.getContentPane().add(ip_scan);
scanned_ip = new JComboBox<>(dtm);
scanned_ip.setBounds(165, 29, 188, 22);
jf.getContentPane().add(scanned_ip);
connect = new JButton("Connect");
connect.setBounds(363, 29, 163, 23);
jf.getContentPane().add(connect);
disconnect = new JButton("Disconnect");
disconnect.setBounds(536, 29, 188, 23);
jf.getContentPane().add(disconnect);
cnt_display = new JLabel("",SwingConstants.CENTER);
cnt_display.setBackground(Color.PINK);
cnt_display.setOpaque(true);
cnt_display.setBounds(165, 509, 361, 28);
jf.getContentPane().add(cnt_display);
addIndicator();
}
void addIndicator()
{
for(int i=0;i<40;i++)
{
ind[i]=new JLabel((char)('A'+(i/8))+""+(i%8),SwingConstants.CENTER);
ind[i].setLayout(null);
ind[i].setOpaque(true);
button_status_panel.add(ind[i]);
}
disconnectIndicator();
}
void connectIndicator()
{
for(int i=0;i<40;i++)
{
iflg[i]=true;
indicate(i,false);
}
updateCount(0);
}
void disconnectIndicator()
{
for(int i=0;i<40;i++)
{
ind[i].setBackground(Color.orange);
}
cnt=-1;
cnt_display.setText("");
}
void indicate(int i,char f)
{
if(f=='1')
{
indicate(i,true);
}
else
{
indicate(i,false);
}
}
void indicate(int i,boolean f)
{
if(iflg[i]==f)
{
return;
}
iflg[i]=f;
ind[i].setBackground(iflg[i]?Color.green:Color.magenta);
}
void updateCount(int a)
{
if(cnt==a)
{
return;
}
cnt=a;
cnt_display.setText(cnt_prefix+cnt);
}
}