package com.practicalexam.student;

import java.util.Scanner;

/**
 *
 * @author FPT University - HCMC Java OOP Practical Exam Template
 */
public class Cabinet {

    //StartList - do not remove this comment!!!
    // Declare ArrayList or Array here
    private Cake ds[] = new Cake[500];
    //EndList - do not remove this comment!!!
    private int count;

    public void add() {
        String id;
        String name;
        double price;
        int size;
        Scanner sc = new Scanner(System.in);
        System.out.println("Add new cake " + (count + 1) + "#" + ds.length);
        do {
            System.out.print("Input id of cake: ");
            id = sc.nextLine();
            if (checkDuplicatedId(id)) {
                System.out.println("Already exist!!");
            }
        } while (checkDuplicatedId(id));
        System.out.print("Input name of cake: ");
        name = sc.nextLine();

        do {
            System.out.print("Input size of cake: ");
            try {
                size = Integer.parseInt(sc.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Error");
            }
        } while (true);

        do {
            System.out.println("Input price: ");
            try {
                price = Double.parseDouble(sc.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Error");
            }
        } while (true);

        if (!checkDuplicatedId(id)) {
            ds[count] = new Cake(id, name, size, price);
            ds[count].showProfile();
            count++;
        }
    }

    public boolean checkDuplicatedId(String id) {
        // Your code here
        for (int i = 0; i < count; i++) {
            if (ds[i].getId().equalsIgnoreCase(id) == true) {
                return true;
            }
        }
        return false;
    }

    public void update() {
        // Print the object details after updating name/model and price
        if (ds.length == 0) {
            System.out.println("Empty");
            return;
        }
        Scanner sc = new Scanner(System.in);
        String id, name;
        double price;
        int check = 0;
        System.out.println("Input id: ");
        id = sc.nextLine();
        for (int i = 0; i < count; i++) {
            if (ds[i].getId().equalsIgnoreCase(id)) {
                System.out.println("Input new name: ");
                name = sc.nextLine();
                ds[i].setName(name);
                do {
                    System.out.println("Input new price: ");
                    try {
                        price = Double.parseDouble(sc.nextLine());
                        break;
                    } catch (Exception e) {
                        System.out.println("Error");
                    }
                } while (true);
                ds[i].setPrice(price);
                ds[i].showProfile();
            } else {
                check = 1;
            }
        }
        if (check == 1) {
            System.out.println("Not found!");
            return;
        }
    }

    public void search() {
        // Print the object details after searching
        if (ds.length == 0) {
            System.out.println("Empty");
            return;
        }
        Scanner sc = new Scanner(System.in);
        String id;
        int check = 0;
        System.out.println("Input id: ");
        id = sc.nextLine();
        for (int i = 0; i < count; i++) {
            if (ds[i].getId().equalsIgnoreCase(id)) {
                ds[i].showProfile();
                return;
            } else {
                check = 1;
            }
        }
        if (check == 1) {
            System.out.println("Not found!");
            return;
        }
    }

    public void remove() {

        if (ds.length == 0) {
            System.out.println("Empty");
            return;
        }// Print the list after removing
        Scanner sc = new Scanner(System.in);
        String id;
        int check = 0;
        System.out.println("Input id: ");
        id = sc.nextLine();
        for (int i = 0; i < count; i++) {
            if (ds[i].getId().equalsIgnoreCase(id)) {
                for (int j = i; j < count - 1; j++) {
                    ds[j] = ds[j + 1];
                }
                // ds[count - 1].showProfile(); //Added
                count--;
                for (int j = 0; j < count; j++) {
                    ds[i].showProfile();
                }
                return;
            } else {
                check = 1;
            }
        }
        if (check == 1) {
            System.out.println("Not found!");
        }

    }

    public void sort() {
        if (ds.length == 0) {
            System.out.println("Empty");
            return;
        }
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count; j++) {
                Cake tmp;
                if (ds[i].getName().compareTo(ds[j].getName()) > 0) {
                    tmp = ds[i];
                    ds[i] = ds[j];
                    ds[j] = tmp;
                }
            }
        }
        for (int i = 0; i < count; i++) {
            ds[i].showProfile();
        }
    }

}
