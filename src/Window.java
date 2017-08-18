import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigInteger;
import java.util.ArrayList;

public class Window extends JFrame {
	private JTextField textFieldGamma;
	private JTextField textFieldP;
	private JTextField textFieldAlpha;
	private JTextField textFieldX;
	private JTextField textFieldY;
	private JPanel mainpanel;
	private JTextField textFieldMsg;
	private JTextField textFieldHash;
	private JButton buttonGenKeys;
	private JButton buttonGenMsg;
	private JTextField textFieldU;
	private JTextField textFieldS;
	private JTextField textFieldZ;
	private JTextField textFieldK;
	private JTextField textFieldG;
	private JButton buttonGenU;
	private JButton buttonGenParams;
	private JTextField textFieldLCheck;
	private JTextField textFieldRCheck;
	private JButton buttonCheckSig;
	private JLabel labelCheckSig;
	private JButton buttonClear;

	private DSignature ds = new DSignature();

	public Window() {
		this.setContentPane(mainpanel);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("ЭЦП вариант 5.1.29");
		this.setSize(new Dimension(1100, 600));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		final ArrayList<JTextField> fields = new ArrayList<JTextField>();
		fields.add(textFieldGamma);
		fields.add(textFieldP);
		fields.add(textFieldAlpha);
		fields.add(textFieldX);
		fields.add(textFieldY);
		fields.add(textFieldMsg);
		fields.add(textFieldHash);
		fields.add(textFieldU);
		fields.add(textFieldZ);
		fields.add(textFieldK);
		fields.add(textFieldG);
		fields.add(textFieldS);
		/***************************************************
		 *
		 *  LISTENERS
		 */
			// BUTTONS
		buttonGenParams.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				String gammaString = textFieldGamma.getText();
				if (gammaString.equals("")) ds.generateGamma(); // генерация всех параметров
				else ds.setGamma(new BigInteger(gammaString)); // ручной ввод гамма
				ds.calculateP();
				ds.generateAlpha();
				textFieldGamma.setText(ds.getGamma().toString());
				textFieldP.setText(ds.getP().toString());
				textFieldAlpha.setText(ds.getAlpha().toString());
			}
		});
		buttonGenKeys.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				String xString = textFieldX.getText();
				if (xString.equals("")) ds.generateX(); // генерация x и вычисление y
				else ds.setX(new BigInteger(xString)); // вычисление y по введённому x
				ds.calculateY();
				textFieldX.setText(ds.getX().toString());
				textFieldY.setText(ds.getY().toString());
			}
		});
		buttonGenMsg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				String msgString = textFieldMsg.getText();
				if (msgString.equals("")) { // Генерация сообщения
					ds.generateMsg();
					ds.getMD5(ds.getMsg().toString());
					textFieldMsg.setText(ds.getMsg().toString());
				}
				else { // ручной ввод сообщения
					ds.getMD5(msgString);
				}
				textFieldHash.setText(ds.getHash().toString());
			}
		});
		buttonGenU.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				String uString = textFieldU.getText();
				if (uString.equals("")) {
					ds.generateU();
				}
				else {
					ds.setU(new BigInteger(uString));
				}
				ds.calculateZ();
				ds.calculateK();
				ds.calculateG();
				ds.calculateS();
				textFieldU.setText(ds.getU().toString());
				textFieldZ.setText(ds.getZ().toString());
				textFieldK.setText(ds.getK().toString());
				textFieldG.setText(ds.getG().toString());
				textFieldS.setText(ds.getS().toString());
			}
		});
		buttonCheckSig.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				for (JTextField jtf : fields) {
					if (jtf.getText().equals("")) {
						JOptionPane.showMessageDialog(null,
								"Заполните все поля",
								"Ошибка",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				ds.calculateLCheck();
				ds.calculateRCheck();
				textFieldLCheck.setText(ds.getlCheck().toString());
				textFieldRCheck.setText(ds.getrCheck().toString());
				labelCheckSig.setText(ds.getrCheck().equals(ds.getlCheck()) ? "Подпись верна" : "Подпись неверна");
			}
		});
		buttonClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				// clear text fields
				labelCheckSig.setText("");
				textFieldLCheck.setText("");
				textFieldRCheck.setText("");
				for (JTextField jtf : fields) jtf.setText("");
				// nullify ds variables
				ds.clear();
			}
		});
			// TEXT FIELDS
		for (JTextField jtf : fields) {
			jtf.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(DocumentEvent documentEvent) {
					ds.setlCheck(null);
					ds.setrCheck(null);
					textFieldLCheck.setText("");
					textFieldRCheck.setText("");
					labelCheckSig.setText("");
				}

				@Override
				public void removeUpdate(DocumentEvent documentEvent) {
					ds.setlCheck(null);
					ds.setrCheck(null);
					textFieldLCheck.setText("");
					textFieldRCheck.setText("");
					labelCheckSig.setText("");
				}

				@Override
				public void changedUpdate(DocumentEvent documentEvent) {

				}
			});
		}
		textFieldGamma.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent focusEvent) {  }

			@Override
			public void focusLost(FocusEvent focusEvent) {
				JTextField current = (JTextField) focusEvent.getSource();
				if (current.getText().equals("")) return;
				else ds.setGamma(new BigInteger(textFieldGamma.getText()));
			}
		});
		textFieldP.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent focusEvent) {  }

			@Override
			public void focusLost(FocusEvent focusEvent) {
				JTextField current = (JTextField) focusEvent.getSource();
				if (current.getText().equals("")) return;
				else ds.setP(new BigInteger(textFieldP.getText()));
			}
		});
		textFieldAlpha.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent focusEvent) {  }

			@Override
			public void focusLost(FocusEvent focusEvent) {
				JTextField current = (JTextField) focusEvent.getSource();
				if (current.getText().equals("")) return;
				else ds.setAlpha(new BigInteger(textFieldAlpha.getText()));
			}
		});
		textFieldX.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent focusEvent) {  }

			@Override
			public void focusLost(FocusEvent focusEvent) {
				JTextField current = (JTextField) focusEvent.getSource();
				if (current.getText().equals("")) return;
				else ds.setX(new BigInteger(textFieldX.getText()));
			}
		});
		textFieldY.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent focusEvent) {  }

			@Override
			public void focusLost(FocusEvent focusEvent) {
				JTextField current = (JTextField) focusEvent.getSource();
				if (current.getText().equals("")) return;
				else ds.setY(new BigInteger(textFieldY.getText()));
			}
		});
		textFieldHash.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent focusEvent) {  }

			@Override
			public void focusLost(FocusEvent focusEvent) {
				JTextField current = (JTextField) focusEvent.getSource();
				if (current.getText().equals("")) return;
				else ds.setHash(new BigInteger(textFieldHash.getText()));
			}
		});
		textFieldU.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent focusEvent) {  }

			@Override
			public void focusLost(FocusEvent focusEvent) {
				JTextField current = (JTextField) focusEvent.getSource();
				if (current.getText().equals("")) return;
				else ds.setU(new BigInteger(textFieldU.getText()));
			}
		});
		textFieldZ.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent focusEvent) {  }

			@Override
			public void focusLost(FocusEvent focusEvent) {
				JTextField current = (JTextField) focusEvent.getSource();
				if (current.getText().equals("")) return;
				else ds.setZ(new BigInteger(textFieldZ.getText()));
			}
		});
		textFieldK.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent focusEvent) {  }

			@Override
			public void focusLost(FocusEvent focusEvent) {
				JTextField current = (JTextField) focusEvent.getSource();
				if (current.getText().equals("")) return;
				else ds.setK(new BigInteger(textFieldK.getText()));
			}
		});
		textFieldG.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent focusEvent) {  }

			@Override
			public void focusLost(FocusEvent focusEvent) {
				JTextField current = (JTextField) focusEvent.getSource();
				if (current.getText().equals("")) return;
				else ds.setG(new BigInteger(textFieldG.getText()));
			}
		});
		textFieldS.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent focusEvent) {  }

			@Override
			public void focusLost(FocusEvent focusEvent) {
				JTextField current = (JTextField) focusEvent.getSource();
				if (current.getText().equals("")) return;
				else ds.setS(new BigInteger(textFieldS.getText()));
			}
		});
	}
}
