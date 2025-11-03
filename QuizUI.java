package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import model.Question;

public class QuizUI extends JFrame implements ActionListener {
    private Question[] questions;
    private int currentIndex = 0;
    private int score = 0;
    private int timeLeft = 15; // seconds per question

    private JLabel questionLabel, timerLabel, statusLabel;
    private JRadioButton[] options;
    private ButtonGroup group;
    private JButton nextButton;
    private Timer timer;

    public QuizUI() {
        setTitle("Online Quiz System ");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Header Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        questionLabel = new JLabel("Question will appear here", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        topPanel.add(questionLabel, BorderLayout.CENTER);

        timerLabel = new JLabel("Time: " + timeLeft + "s", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setForeground(Color.RED);
        topPanel.add(timerLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel for Options
        JPanel centerPanel = new JPanel(new GridLayout(4, 1));
        options = new JRadioButton[4];
        group = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setFont(new Font("Times New Roman", Font.PLAIN, 16));
            group.add(options[i]);
            centerPanel.add(options[i]);
        }
        add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        nextButton = new JButton("Next");
        nextButton.addActionListener(this);
        bottomPanel.add(nextButton, BorderLayout.EAST);

        statusLabel = new JLabel("Question 1 of 5", SwingConstants.LEFT);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        bottomPanel.add(statusLabel, BorderLayout.WEST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load Questions
        loadQuestions();
        displayQuestion();

        // Start Timer
        startTimer();

        setVisible(true);
    }

    private void loadQuestions() {
        questions = new Question[]{
            new Question("Which language is used for web apps?", new String[]{"Python", "Java", "JavaScript", "C++"}, 2),
            new Question("Which company developed Java?", new String[]{"Google", "Sun Microsystems", "Microsoft", "Apple"}, 1),
            new Question("What does HTML stand for?", new String[]{"Hyper Trainer Marking Language", "Hyper Text Markup Language", "High Text Markup Language", "None"}, 1),
            new Question("Which keyword is used to inherit a class in Java?", new String[]{"super", "this", "extends", "implement"}, 2),
            new Question("Which of the following is not an OOP concept?", new String[]{"Encapsulation", "Abstraction", "Compilation", "Inheritance"}, 2)
        };
    }

    private void displayQuestion() {
        if (currentIndex < questions.length) {
            Question q = questions[currentIndex];
            questionLabel.setText("Q" + (currentIndex + 1) + ": " + q.getQuestionText());
            String[] opts = q.getOptions();
            for (int i = 0; i < 4; i++) {
                options[i].setText(opts[i]);
            }
            group.clearSelection();
            statusLabel.setText("Question " + (currentIndex + 1) + " of " + questions.length);
        } else {
            endQuiz();
        }
    }

    private void startTimer() {
        timer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft + "s");
            if (timeLeft <= 0) {
                nextQuestion();
            }
        });
        timer.start();
    }

    private void nextQuestion() {
        checkAnswer();
        currentIndex++;
        if (currentIndex < questions.length) {
            timeLeft = 15;
            displayQuestion();
        } else {
            endQuiz();
        }
    }

    private void checkAnswer() {
        int selected = -1;
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected()) {
                selected = i;
                break;
            }
        }

        if (selected == questions[currentIndex].getCorrectAnswerIndex()) {
            score++;
        }
    }

    private void endQuiz() {
        timer.stop();
        for (JRadioButton option : options) option.setEnabled(false);
        nextButton.setEnabled(false);
        questionLabel.setText("Quiz Over!");
        timerLabel.setText("");
        statusLabel.setText("Your Score: " + score + "/" + questions.length);
        JOptionPane.showMessageDialog(this, "Your Final Score: " + score + " / " + questions.length);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        nextQuestion();
    }
}
