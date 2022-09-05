package main;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.SwingConstants;

import org.apache.log4j.BasicConfigurator;

import data.Person;
import helper.XMLConvert;

import javax.swing.JTextArea;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Sender extends JFrame implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton btnSender;
	private JTextArea txtMessage;
	public static void main(String[] args) {
		Sender sender = new Sender();
		sender.setVisible(true);
	}
	/**
	 * Create the panel.
	 */
	public Sender() {
		setLayout(null);
		setSize(700, 500);
		JLabel lblNewLabel = new JLabel("SENDER");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblNewLabel.setBounds(10, 11, 680, 45);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Message");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_1.setBounds(45, 102, 78, 29);
		add(lblNewLabel_1);
		
		txtMessage = new JTextArea();
		txtMessage.setFont(new Font("Arial", Font.PLAIN, 15));
		txtMessage.setBounds(162, 106, 485, 207);
		add(txtMessage);
		
		btnSender = new JButton("Send");
		btnSender.setBounds(359, 342, 117, 35);
		add(btnSender);
		btnSender.addMouseListener(this);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		Object  o = e.getSource();
		if(o == btnSender) {
			System.out.println("OKe");
			try {
				//config environment for JMS
				BasicConfigurator.configure();
				//config environment for JNDI
				Properties settings = new Properties();
				settings.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
				settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
				//create context
				Context ctx = new InitialContext(settings);
				//lookup JMS connection factory
				ConnectionFactory factory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
				//lookup destination. (If not exist-->ActiveMQ create once)
				Destination destination = (Destination) ctx.lookup("dynamicQueues/thanthidet");
				//get connection using credential
				Connection con = factory.createConnection("admin", "admin");
				//connect to MOM
				con.start();
				//create session
				Session session = con.createSession(/* transaction */false, /* ACK */Session.AUTO_ACKNOWLEDGE);
				//create producer
				MessageProducer producer = session.createProducer(destination);
				//create text message
				Message msg = session.createTextMessage("hello mesage from ActiveMQ");
				producer.send(msg);
			
				try {
					String message = txtMessage.getText();
					Person p =  new Person(19531531,"Trần Tấn Phước",new Date(),message);
					String xml = new XMLConvert<Person>(p).object2XML(p);
					msg = session.createTextMessage(xml);
					producer.send(msg);
				} finally {
					session.close();
					con.close();
					System.out.println("Finished...");
				}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
