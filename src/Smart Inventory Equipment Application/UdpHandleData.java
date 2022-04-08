import java.awt.event.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;
class UdpHandleData
{
TraySimulatorApplication tsa = new TraySimulatorApplication();
DeviceScanner ds = new DeviceScanner();
DatagramSocket dso;
SynchronousQueue<String> cmd_sq = new SynchronousQueue<>();
SynchronousQueue<Boolean> listen_cmd_sq=new SynchronousQueue<>();
Thread loop = new Thread()
{
public void run()
{
try
{
dso=new DatagramSocket();
dso.setSoTimeout(1);
SocketAddress sa=null;
boolean con_flg=false;
String tx_cmd="D:REQUEST STATE:SMART_EQUIPMENT_TRAY";
long snp=0;
while(true)
{
try
{
String cmd=cmd_sq.poll();
if(cmd!=null)
{
if(cmd.indexOf("connect:")==0)
{
sa=new InetSocketAddress(cmd.substring("connect:".length()),4210);
tsa.connectIndicator();
con_flg=true;
}
else if(cmd.equals("disconnect") || cmd.equals("scan device"))
{
con_flg=false;
tsa.disconnectIndicator();
if(cmd.equals("scan device"))
{
scanDevices();
}
}
}
if(con_flg && (System.currentTimeMillis()-snp)>=350)
{
DatagramPacket dp = new DatagramPacket(tx_cmd.getBytes(),0,tx_cmd.length());
dp.setSocketAddress(sa);
dso.send(dp);
snp=System.currentTimeMillis();
}
try
{
byte rec[] = new byte[128];
DatagramPacket dp = new DatagramPacket(rec,rec.length);
dso.receive(dp);
if(con_flg)
{
int len_break="D:STATE:".length();
String rx_cmd=new String(dp.getData(),0,len_break);
if(rx_cmd.equals("D:STATE:"))
{
rec=Arrays.copyOfRange(dp.getData(),len_break,dp.getLength());
ByteBuffer bb = ByteBuffer.wrap(rec);
char arr[] = completeBinary(bb.getLong(),40).toCharArray();
int cnt=0;
for(int i=0;i<40;i++)
{
tsa.indicate(i,arr[i]);
if(arr[i]=='1')
{
cnt++;
}
}
tsa.updateCount(cnt);
}
}
}
catch(Exception eeee)
{
}
}
catch(Exception e)
{
e.printStackTrace(System.out);
}
}
}
catch(Exception ee)
{
}
}
}
;
UdpHandleData()
{
addFunctionality();
loop.start();
try
{
cmd_sq.put("scan device");
}
catch(Exception e)
{
}
}
String completeBinary(long a,int b)
{
String bin=Long.toBinaryString(a);
while(bin.length()!=b)
{
bin="0"+bin;
}
return bin;
}
void addFunctionality()
{
tsa.ip_scan.addActionListener(new ActionListener()
{
public void actionPerformed(ActionEvent e)
{
try
{
cmd_sq.put("scan device");
}
catch(Exception ee)
{
}
}
}
);
tsa.connect.addActionListener(new ActionListener()
{
public void actionPerformed(ActionEvent e)
{
try
{
String cmd=(String)tsa.dtm.getSelectedItem();
cmd_sq.put("connect:"+cmd);
}
catch(Exception ee)
{
}
}
}
);
tsa.disconnect.addActionListener(new ActionListener()
{
public void actionPerformed(ActionEvent e)
{
try
{
cmd_sq.put("disconnect");
}
catch(Exception ee)
{
}
}
}
);
}
void scanDevices()
{
tsa.ip_scan.setEnabled(false);
tsa.connect.setEnabled(false);
tsa.disconnect.setEnabled(false);
try
{
tsa.dtm.removeAllElements();
for(String str : ds.scanDevice("D:ECHO:SMART_EQUIPMENT_TRAY",4210,4000,4))
{
tsa.dtm.addElement(str);
}
}
catch(Exception e)
{
}
tsa.ip_scan.setEnabled(true);
tsa.connect.setEnabled(true);
tsa.disconnect.setEnabled(true);
}
}