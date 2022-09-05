package data;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = { "mssv", "hoten", "ngaysinh","message" })
public class Person implements Serializable {
	private long mssv;
	private String hoten;
	private Date ngaysinh;
	private String message;

	public Person(long mssv, String hoten, Date ngaysinh,String message) {
		this.mssv = mssv;
		this.hoten = hoten;
		this.ngaysinh = ngaysinh;
		this.message=message;
	}

	public Person() {
	}

	public long getMssv() {
		return mssv;
	}

	public void setMssv(long mssv) {
		this.mssv = mssv;
	}

	public String getHoten() {
		return hoten;
	}

	public void setHoten(String hoten) {
		this.hoten = hoten;
	}

	public Date getNgaysinh() {
		return ngaysinh;
	}

	public void setNgaysinh(Date ngaysinh) {
		this.ngaysinh = ngaysinh;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Person [mssv=" + mssv + ", hoten=" + hoten + ", ngaysinh=" + ngaysinh + ", message=" + message + "]";
	}


	

}
