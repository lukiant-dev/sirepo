package com.sample;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;

/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {

	private static String title = "Potrawa dla Ciebie";

	public static final void main(String[] args) {
		try {
			// load up the knowledge base
			KnowledgeBase kbase = readKnowledgeBase();
			StatefulKnowledgeSession ksession = kbase
					.newStatefulKnowledgeSession();
			KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory
					.newFileLogger(ksession, "test");
			// go !

			ksession.fireAllRules();
			logger.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private static KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("Sample.drl"),
				ResourceType.DRL);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}

	public static class Question {

		public static boolean yesOrNo(String question) {
			int answer = JOptionPane.showConfirmDialog(null, question, title,
					JOptionPane.YES_NO_OPTION);
			if (answer == 0)
				return true;
			return false;
		}

		public static ArrayList<String> fewOptions(String[] options,
				String question) {

			int amount = options.length;
			int i = 0;
			ArrayList<String> results = new ArrayList<String>();
			JPanel qPanel = new JPanel(new GridLayout(amount + 1, 1));
			qPanel.add(new JLabel(question));
			JCheckBox[] chBoxes = new JCheckBox[amount];
			for (String str : options) {
				chBoxes[i] = new JCheckBox(str);
				qPanel.add(chBoxes[i]);
				i++;
			}
			/* sprawdzenie czy chociaż jeden checkboxow został wybrany */
			//while (true) {

				JOptionPane.showMessageDialog(null, qPanel, title,
						JOptionPane.PLAIN_MESSAGE);

				for (JCheckBox chBox : chBoxes)
					if (chBox.isSelected())
						results.add(chBox.getText());
/*
				if (results.size() > 0)
					break;*/

			/*	JOptionPane.showMessageDialog(null,
						"Zaznacz chociaz jedną z podanych odpowiedzi", title,
						JOptionPane.ERROR_MESSAGE);

*/			//}

			return results;
		}

		public static ArrayList<String> oneFromFewOption(String[] options, String question) {

			int amount = options.length;
			int i = 0;
			JPanel qPanel = new JPanel(new GridLayout(amount + 1, 1));
			ArrayList<String> results = new ArrayList<String>();
			qPanel.add(new JLabel(question));
			JRadioButton[] rButtons = new JRadioButton[amount];
			ButtonGroup btnGroup = new ButtonGroup();
			for (String str : options) {
				rButtons[i] = new JRadioButton(str);
				btnGroup.add(rButtons[i]);
				qPanel.add(rButtons[i]);
				i++;
			}
			rButtons[0].setSelected(true);
			JOptionPane.showMessageDialog(null, qPanel, title,
					JOptionPane.PLAIN_MESSAGE);

			for (JRadioButton rBtn : rButtons)
				if (rBtn.isSelected())
					 results.add(rBtn.getText());

			return results;

		}

		public static void displayResult(String result) {
			String res = result;
			JOptionPane.showMessageDialog(null, res, title,
					JOptionPane.PLAIN_MESSAGE);
		}
	}

	public static class Attribute {
		public String my_id;
		public ArrayList<String> parameters = new ArrayList<String>();

		public Attribute(String new_id) {

			my_id = new_id;
		}
		
		public void getParams(ArrayList<String> params) {
			if (params != null)
				for (String str : params) {
					parameters.add(str);
					System.out.println(str);
				}
		}
	}

}
