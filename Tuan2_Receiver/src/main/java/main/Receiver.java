package main;

import java.awt.EventQueue;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.BasicConfigurator;

import javax.swing.JTextArea;

public class Receiver extends JFrame {

	private JPanel contentPane;
	private JTextArea txtReceiver;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Receiver frame = new Receiver();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Receiver() throws Exception {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		txtReceiver = new JTextArea();
		txtReceiver.setBounds(10, 11, 950, 625);
		contentPane.add(txtReceiver);

		// thiết lập môi trường cho JMS
		BasicConfigurator.configure();
		// thiết lập môi trường cho JJNDI
		Properties settings = new Properties();
		settings.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
		// tạo context
		Context ctx = new InitialContext(settings);
		// lookup JMS connection factory
		Object obj = ctx.lookup("ConnectionFactory");
		ConnectionFactory factory = (ConnectionFactory) obj;
		// lookup destination
		Destination destination = (Destination) ctx.lookup("dynamicQueues/thanthidet");
		// tạo connection
		Connection con = factory.createConnection("admin", "admin");
		// nối đến MOM
		con.start();
		// tạo session
		Session session = con.createSession(/* transaction */false, /* ACK */Session.CLIENT_ACKNOWLEDGE);
		// tạo consumer
		MessageConsumer receiver = session.createConsumer(destination);
		// blocked-method for receiving message - sync
		// receiver.receive();
		// Cho receiver lắng nghe trên queue, chừng có message thì notify - async
		receiver.setMessageListener(new MessageListener() {

			// có message đến queue, phương thức này được thực thi
			public void onMessage(Message msg) {// msg là message nhận được
				try {
					String txt;
					if (msg instanceof TextMessage) {
						TextMessage tm = (TextMessage) msg;
						txt = tm.getText();
						System.out.println("Nhận được " + txt);
						System.out.println("_________________________");
						txtReceiver.append("Nhận được" + txt+"\n");
						msg.acknowledge();// gửi tín hiệu ack
					} else if (msg instanceof ObjectMessage) {
						ObjectMessage om = (ObjectMessage) msg;
						txtReceiver.setText(""+om);
					}
					// others message type....
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
