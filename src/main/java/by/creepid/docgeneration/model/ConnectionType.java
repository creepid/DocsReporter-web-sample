package by.creepid.docgeneration.model;

public enum ConnectionType {
	web("web"), offline("offline"), unknown("");

	private String title;

	private ConnectionType(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public static ConnectionType getType(String title) {
		ConnectionType type = unknown; 
		
		if (title == null) {
			return type;
		}

		ConnectionType[] types = ConnectionType.values();

		for (ConnectionType connectionType : types) {
			if (connectionType.getTitle().equalsIgnoreCase(title)) {
				type = connectionType;
				break;
			}
		}

		return type;
	}

}
