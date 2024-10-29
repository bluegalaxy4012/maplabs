public class Inmultire extends Operatie{
    @Override
    public NumarComplex opereaza(NumarComplex nr1, NumarComplex nr2) {
        return new NumarComplex(nr1.real*nr2.real - nr1.imaginar*nr2.imaginar, nr1.real*nr2.imaginar + nr1.imaginar*nr2.real);
    }
}
