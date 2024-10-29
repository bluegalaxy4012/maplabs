public class Impartire extends Operatie{
    @java.lang.Override
    public NumarComplex opereaza(NumarComplex nr1, NumarComplex nr2) {
        //ampl conjugat, presupun ca nu i 0 nr2
        float numitorNou = nr2.real*nr2.real + nr2.imaginar*nr2.imaginar;
        float real = (nr1.real*nr2.real + nr1.imaginar*nr2.imaginar)/numitorNou;
        float imaginar = (nr1.imaginar*nr2.real - nr1.real*nr2.imaginar)/numitorNou;

        return new NumarComplex(real, imaginar);
    }
}