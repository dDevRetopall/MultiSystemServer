package utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EncriptarPasswords {
	
	
	public static String encriptarPassword(String pwd){
		MessageDigest md;
		 try {
		        md = MessageDigest.getInstance("MD5");
		        byte[] passBytes = pwd.getBytes();
		        String string="";
		        for(byte b:passBytes){
		        	string=string+b;
		        }
		        System.out.println(string);
		        md.reset();
		        byte[] digested = md.digest(passBytes);
		        string="";
		        for(byte b:passBytes){
		        	string=string+b;
		        }
		        System.out.println(string);
		        StringBuffer sb = new StringBuffer();
		        for(int i=0;i<digested.length;i++){
		            sb.append(Integer.toHexString(0xff & digested[i]));
		        }
		        return sb.toString();
		    } catch (NoSuchAlgorithmException ex) {
		        Logger.getLogger(EncriptarPasswords.class.getName()).log(Level.SEVERE, null, ex);
		    }
		return null;
	}
	
}
