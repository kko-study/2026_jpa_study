package homework.chapter_6;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Team {

	@Id
	@Column(name = "TEAM_ID")
	private String id;

	private String name;

	@OneToMany
	private List<Member> members = new ArrayList<>();

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Team{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", members.size()=" + members.size() +
				'}';
	}
}