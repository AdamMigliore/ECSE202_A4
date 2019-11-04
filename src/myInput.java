import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class myInput extends JPanel{
	
	private JTextField textField;
	
	public myInput(String tagName, Double minValue, Double maxValue, Double value) {
		
		this.setLayout(new GridLayout(1, 4));

		add(new JLabel(tagName));
		add(new JLabel(minValue.toString()));
		textField = new JTextField(value.toString());
		add(textField);
		add(new JLabel(maxValue.toString()));
	}
	
	public myInput(String tagName, Integer minValue, Integer maxValue, Integer value) {
		
		this.setLayout(new GridLayout(1, 4));

		add(new JLabel(tagName));
		add(new JLabel(minValue.toString()));
		textField = new JTextField(value.toString());
		add(textField);
		add(new JLabel(maxValue.toString()));
	}
	
	public double getTextFieldValue() {
		return Double.parseDouble(textField.getText());
	}
}
