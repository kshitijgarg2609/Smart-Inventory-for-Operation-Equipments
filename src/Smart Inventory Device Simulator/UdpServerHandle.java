import java.awt.event.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import java.util.concurrent.*;
class UdpServerHandle
{
DatagramSocket ds;
SynchronousQueue<String> cmd_sq = new SynchronousQueue<>(true);
TraySimulator40 ts = new TraySimulator40();
Thread loop = new Thread()
{
char button_track[] = new char[40];
public void run()
{
try
{
Arrays.fill(button_track,'1');
ds=new DatagramSocket(4210);
ds.setSoTimeout(1);
while(true)
{
String cmd=cmd_sq.poll();
if(cmd!=null)
{
if(cmd.indexOf("TOGGLE:")==0)
{
cmd=cmd.substring("TOGGLE:".length());
int i=Integer.parseInt(cmd.substring(0,2));
int j=Integer.parseInt(cmd.substring(2,4));
button_track[(i*8)+j]=cmd.charAt(4);
}
}
try
{
byte rec[] = new byte[128];
DatagramPacket dp = new DatagramPacket(rec,rec.length);
ds.receive(dp);
String rstr=new String(dp.getData(),0,dp.getLength());
if(rstr.equals("D:REQUEST STATE:SMART_EQUIPMENT_TRAY"))
{
ByteBuffer bb = ByteBuffer.allocate(16);
bb.put((byte)('D'));
bb.put((byte)(':'));
bb.put((byte)('S'));
bb.put((byte)('T'));
bb.put((byte)('A'));
bb.put((byte)('T'));
bb.put((byte)('E'));
bb.put((byte)(':'));
bb.putLong(Long.parseLong(new String(button_track),2));
sendUdpReply(dp.getSocketAddress(),bb.array());
}
else  if(rstr.equals("D:ECHO:SMART_EQUIPMENT_TRAY"))
{
sendUdpReply(dp.getSocketAddress(),"D:ECHO:SMART_EQUIPMENT_TRAY");
}
}
catch(Exception ee)
{
}
}
}
catch(Exception e)
{
}
}
void sendUdpReply(SocketAddress sa,String s)
{
sendUdpReply(sa,s.getBytes());
}
void sendUdpReply(SocketAddress sa,byte s[])
{
try
{
DatagramPacket dp = new DatagramPacket(s,0,s.length);
dp.setSocketAddress(sa);
ds.send(dp);
}
catch(Exception e)
{
}
}
}
;
class EventToggle implements ItemListener
{
String t_id;
EventToggle(Integer i,Integer j)
{
t_id=String.format("TOGGLE:%02d%02d",new Object[]{i,j});
}
public void itemStateChanged(ItemEvent i)
{
try
{
if(i.getStateChange()==ItemEvent.SELECTED)
{
cmd_sq.put(t_id+"1");
}
else
{
cmd_sq.put(t_id+"0");
}
}
catch(Exception e)
{
}
}
}
UdpServerHandle()
{
addFunctionality();
loop.start();
}
void addFunctionality()
{
for(int i=0;i<5;i++)
{
for(int j=0;j<8;j++)
{
ts.equip[i][j].addItemListener(new EventToggle(i,j));
}
}
}
}