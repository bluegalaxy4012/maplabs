public class Scadere extends Operatie{
    @Override
    public NumarComplex opereaza(NumarComplex nr1, NumarComplex nr2) {
        return new NumarComplex(nr1.real-nr2.real, nr1.imaginar-nr2.imaginar);
    }
}
