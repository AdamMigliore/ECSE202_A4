import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;



/**
 * This class is used as a template for a line in the simulation parameters panel
 * @author adamd 
 *
 */
public class myInput extends JPanel{
	
	private JTextField textField;//the textfield used by the user to enter a value
	
	/**
	 * @param tagName : label that describes what the panel is about
	 * @param minValue : the minimum value allowed in the textBox
	 * @param maxValue : : the maximum value allowed in the textBox
	 * @param value : : the initial value in the textBox
	 */
	public myInput(String tagName, Double minValue, Double maxValue, Double value) {
		
		this.setLayout(new GridLayout(1, 4));

		add(new JLabel(tagName));
		add(new JLabel(minValue.toString()));
		textField = new JTextField(value.toString());
		add(textField);
		add(new JLabel(maxValue.toString()));
	}
	
	/**
	 * This is a integer version of the panel for the inputs that require integers
	 * @param tagName : label that describes what the panel is about
	 * @param minValue : the minimum value allowed in the textBox
	 * @param maxValue : : the maximum value allowed in the textBox
	 * @param value : : the initial value in the textBox
	 */
	public myInput(String tagName, Integer minValue, Integer maxValue, Integer value) {
		
		this.setLayout(new GridLayout(1, 4));

		add(new JLabel(tagName));
		add(new JLabel(minValue.toString()));
		textField = new JTextField(value.toString());
		add(textField);
		add(new JLabel(maxValue.toString()));
	}
	
	/**
	 * @return a double conversion of the text entered in the textfield
	 */
	public double getTextFieldValue() {
		return Double.parseDouble(textField.getText());
	}
}
