package domain.hostinghealthcheckerapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DomainHostingHealthCheckerGUI extends JFrame {

    // Data arrays
    String[] domainNames = {"sketchsquad.co.za", "nedmedia.co.za", "taliloops.co.za", "locallegends.org", "vhutali.dev"};
    String[] hostingProviders = {"Afrihost", "Hostinger", "GoDaddy", "Xneelo", "Bluehost"};
    String[] dnsStatus = {"Connected", "Pending", "Connected", "Error", "Connected"};
    String[] sslStatus = {"Valid", "Expired", "Valid", "Missing", "Valid"};

    JTextArea displayArea;
    JTextField searchField;

    public DomainHostingHealthCheckerGUI() {
        // Frame settings
        setTitle("Domain Hosting Health Checker");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === TOP PANEL ===
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        JLabel titleLabel = new JLabel("Domain Hosting Health Checker", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Nexa", Font.BOLD, 20));
        topPanel.add(titleLabel);

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search Domain: "));
        searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        topPanel.add(searchPanel);

        add(topPanel, BorderLayout.NORTH);

        // === CENTER TEXT AREA ===
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // === BUTTON PANEL ===
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));

        JButton viewBtn = new JButton("View All Domains");
        JButton updateBtn = new JButton("Update Domain");
        JButton healthyBtn = new JButton("Count Healthy Domains");
        JButton analyticsBtn = new JButton("Analytics Summary");
        JButton clearBtn = new JButton("Clear");
        JButton exitBtn = new JButton("Exit");

        buttonPanel.add(viewBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(healthyBtn);
        buttonPanel.add(analyticsBtn);
        buttonPanel.add(exitBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // === ACTION LISTENERS ===
        viewBtn.addActionListener(e -> viewAllDomains());
        updateBtn.addActionListener(e -> updateDomain());
        searchBtn.addActionListener(e -> searchDomain());
        healthyBtn.addActionListener(e -> countHealthyDomains());
        analyticsBtn.addActionListener(e -> showAnalytics());
        clearBtn.addActionListener(e -> displayArea.setText(""));
        exitBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    // === METHODS ===
    private void viewAllDomains() {
        displayArea.setText("=== All Domain Information ===\n\n");
        for (int i = 0; i < domainNames.length; i++) {
            displayArea.append((i + 1) + ". " + domainNames[i] + "\n"
                    + "   Hosting: " + hostingProviders[i] + "\n"
                    + "   DNS: " + dnsStatus[i] + "\n"
                    + "   SSL: " + sslStatus[i] + "\n\n");
        }
    }

    private void updateDomain() {
        try {
            String indexStr = JOptionPane.showInputDialog(this, "Enter domain number to update (1-5):");
            if (indexStr == null) return;
            int index = Integer.parseInt(indexStr) - 1;

            if (index < 0 || index >= domainNames.length) {
                JOptionPane.showMessageDialog(this, "Invalid domain number.");
                return;
            }

            String newDomain = JOptionPane.showInputDialog(this, "Enter new domain name:", domainNames[index]);
            String newProvider = JOptionPane.showInputDialog(this, "Enter new hosting provider:", hostingProviders[index]);
            String newDNS = JOptionPane.showInputDialog(this, "Enter new DNS Status:", dnsStatus[index]);
            String newSSL = JOptionPane.showInputDialog(this, "Enter new SSL Status:", sslStatus[index]);

            domainNames[index] = newDomain;
            hostingProviders[index] = newProvider;
            dnsStatus[index] = newDNS;
            sslStatus[index] = newSSL;

            JOptionPane.showMessageDialog(this, "Domain updated successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Update cancelled or invalid input.");
        }
    }

    private void searchDomain() {
        String search = searchField.getText().trim();
        if (search.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a domain to search.");
            return;
        }

        boolean found = false;
        for (int i = 0; i < domainNames.length; i++) {
            if (domainNames[i].equalsIgnoreCase(search)) {
                displayArea.setText("Domain found:\n\n" +
                        "Name: " + domainNames[i] + "\n" +
                        "Hosting: " + hostingProviders[i] + "\n" +
                        "DNS: " + dnsStatus[i] + "\n" +
                        "SSL: " + sslStatus[i]);
                found = true;
                break;
            }
        }

        if (!found) {
            displayArea.setText("Domain not found.");
        }
    }

    private void countHealthyDomains() {
        int healthyCount = 0;
        for (int i = 0; i < domainNames.length; i++) {
            if (dnsStatus[i].equalsIgnoreCase("Connected") && sslStatus[i].equalsIgnoreCase("Valid")) {
                healthyCount++;
            }
        }
        displayArea.setText("Total Healthy Domains: " + healthyCount);
    }

    private void showAnalytics() {
        displayArea.setText("--- Analytics Summary ---\n");

        int totalDomains = domainNames.length;
        int expiredSSL = 0;
        int dnsErrors = 0;

        for (int i = 0; i < totalDomains; i++) {
            if (sslStatus[i].equalsIgnoreCase("Expired")) expiredSSL++;
            if (dnsStatus[i].equalsIgnoreCase("Error")) dnsErrors++;
        }

        displayArea.append("\nTotal Domains: " + totalDomains);
        displayArea.append("\nExpired SSLs: " + expiredSSL);
        displayArea.append("\nDNS Errors: " + dnsErrors);

        String[] uniqueProviders = {"Afrihost", "Hostinger", "GoDaddy", "Xneelo", "Bluehost"};
        int[] providerCount = new int[uniqueProviders.length];

        for (int i = 0; i < hostingProviders.length; i++) {
            for (int j = 0; j < uniqueProviders.length; j++) {
                if (hostingProviders[i].equalsIgnoreCase(uniqueProviders[j])) {
                    providerCount[j]++;
                }
            }
        }

        int maxIndex = 0;
        for (int i = 1; i < providerCount.length; i++) {
            if (providerCount[i] > providerCount[maxIndex]) maxIndex = i;
        }

        displayArea.append("\nMost Common Provider: " + uniqueProviders[maxIndex]);

        displayArea.append("\n\nFlagged Domains:\n");
        int flagged = 0;
        for (int i = 0; i < totalDomains; i++) {
            if (dnsStatus[i].equalsIgnoreCase("Error") || sslStatus[i].equalsIgnoreCase("Missing")) {
                displayArea.append(domainNames[i] + " (DNS or SSL issue)\n");
                flagged++;
            }
        }
        displayArea.append("Total Flagged: " + flagged);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DomainHostingHealthCheckerGUI::new);
    }
}
