import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class lab1 {

    public static void main(String[] args) {
        Boolean valid = true;

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String[] tokens = input.split(" ");

        List<NumarComplex> numere = new ArrayList<>();
        List<Operatie> operatori = new ArrayList<>();

        for (var token : tokens) {
            NumarComplex nr;

            if (token.length() == 1) {
                switch (token) {
                    case "+":
                        operatori.add(new Adunare());
                        break;

                    case "-":
                        operatori.add(new Scadere());
                        break;

                    case "*":
                        operatori.add(new Inmultire());
                        break;

                    case "/":
                        operatori.add(new Impartire());
                        break;
                    default:
                        valid = false;
                        break;
                }
            } else {
                valid = getComplexNumber(token, numere, valid);
            }
        }

        if (!valid || numere.size() == 0)
            System.out.println("expresie invalida");
        else {
            NumarComplex rezultat = numere.get(0);
            for (int i = 1; i < numere.size(); i++) {
                NumarComplex deoperat = numere.get(i);
                if (operatori.get(i - 1).getClass() != operatori.get(0).getClass()) {
                    valid = false;
                    System.out.println("expresie invalida(op)"); //ca s mai multi operatori
                    break;
                }
                rezultat = operatori.get(i - 1).opereaza(rezultat, deoperat);
            }

            if (valid)
                System.out.println(rezultat);
        }
    }

    private static Boolean getComplexNumber(String token, List<NumarComplex> numere, Boolean valid) {
        NumarComplex nr;
        token = token.substring(0, token.length() - 1);
        //System.out.println("token e "+token);
        int ultimPlus = token.lastIndexOf("+");
        int ultimMinus = token.lastIndexOf("-");
        String realString, imaginarString;
        if (ultimMinus < 1) {
            realString = token.substring(0, ultimPlus);
            imaginarString = token.substring(ultimPlus + 1);
            //System.out.println(real + "/" + imaginar);
        } else {
            realString = token.substring(0, ultimMinus);
            imaginarString = token.substring(ultimMinus);
            //System.out.println(real + "/" + imaginar);

        }

        try {
            float real = Float.parseFloat(realString);
            float imaginar = Float.parseFloat(imaginarString);
            //System.out.println("real/img "+real + "/" + imaginar);
            nr = new NumarComplex(real, imaginar);
            numere.add(nr);
        } catch (NumberFormatException e) {
            valid = false;
        }
        return valid;
    }
}