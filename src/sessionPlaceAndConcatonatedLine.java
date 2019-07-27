
public class sessionPlaceAndConcatonatedLine {


	private static int place;
	private static String concatonatedLine;
	
	public sessionPlaceAndConcatonatedLine(int place, String concatonatedLine) {
		sessionPlaceAndConcatonatedLine.place = place;
		sessionPlaceAndConcatonatedLine.concatonatedLine = concatonatedLine;
	}
	
	public int getPlace() {
		return place;
	}
	
	public void incPlace() {
		place++;
	}

	public String getConcatonatedLine() {
		return concatonatedLine;
	}

	public void setConcatonatedLine(String concatonatedLine) {
		sessionPlaceAndConcatonatedLine.concatonatedLine = concatonatedLine;
	}

}
