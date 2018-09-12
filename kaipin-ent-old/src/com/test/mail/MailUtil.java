package com.test.mail;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil {

	static int port = 587;

	static String server = "smtp.exmail.qq.com";// 邮件服务器mail.cpip.net.cn

	static String from = "张三";// 发送者,显示的发件人名字

	static String user = "1059976050@qq.com";// 发送者邮箱地址

	static String password = "mlp940";// 密码

	public static void sendEmail(String email, String subject, String body) throws UnsupportedEncodingException {

		try {

			Properties props = new Properties();

			props.put("mail.smtp.host", server);

			props.put("mail.smtp.port", "465");

			props.put("mail.smtp.auth", "true");

			Transport transport = null;

			Session session = Session.getDefaultInstance(props, null);

			transport = session.getTransport("smtp");

			transport.connect(server, user, password);

			MimeMessage msg = new MimeMessage(session);

			msg.setSentDate(new Date());

			InternetAddress fromAddress = new InternetAddress(user, from, "UTF-8");

			msg.setFrom(fromAddress);

			InternetAddress[] toAddress = new InternetAddress[1];

			toAddress[0] = new InternetAddress(email);

			msg.setRecipients(Message.RecipientType.TO, toAddress);

			msg.setSubject(subject, "UTF-8");

			msg.setText(body, "UTF-8");

			msg.saveChanges();

			transport.sendMessage(msg, msg.getAllRecipients());

			transport.close();

		} catch (NoSuchProviderException e) {

			e.printStackTrace();

		} catch (MessagingException e) {

			e.printStackTrace();

		}

	}

	public static void main(String args[]) throws UnsupportedEncodingException

	{

		sendEmail("382576884@qq.com", "淫荡的人", "猥琐！！");// 收件人

		System.out.println("ok");

	}
}