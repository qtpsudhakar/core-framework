package com.automation.utility;

import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class PasswordEncrypter {

	public static void main(String[] args) throws Exception {
		if (args.length > 0) {
			switch (Integer.parseInt(args[0])) {
			case 1:
				System.out.print("Enter Password to get encrypted : ");
				Scanner scC = new Scanner(System.in);
				String plain = scC.nextLine();
				System.out.println("Encrypted Password: " + CryptString.encrypt(plain));
				break;
			case 2:
				System.out.print("Enter Password to decrypt : ");
				Scanner scD = new Scanner(System.in);
				String encrypted = scD.nextLine();
				System.out.println("Plain Password after decryption : " + CryptString.decrypt(encrypted));
				break;
			default:
				System.out.println("This option is not recognized, Enter 1 or 2 ");
				break;
			}
			System.out.print("Press 'ENTER' key to exit \n");
			Scanner sExit = new Scanner(System.in);
			sExit.nextLine();
			sExit.close();
		} else {
			JPasswordField pf = new JPasswordField();
			int plain = JOptionPane.showConfirmDialog(null, pf, "Enter Password", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE);
			if (plain == JOptionPane.OK_OPTION) {
				String password = new String(pf.getPassword());
				String enc = CryptString.encrypt(password);
				System.out.println("Encrypted text: " + enc);
			}
		}
	}
}
