import java.io.Serializable;

public class Student implements Serializable {
	
	public String id;
	public String name;
	public String phone;
	
	public Student() {}
	
	public Student(String id, String name, String phone) {
		
		this.id = id;
		this.name = name;
		this.phone = phone;
	}


}
