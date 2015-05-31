import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

/**
 * Created by Kazako on 31.05.2015.
 */
public class BDDTest {

    BDDFactory bddFactory = BDDFactory.init(3, 3);

    public void quickTest() {
        bddFactory.setVarNum(3);
        BDD a = bddFactory.ithVar(0);
        BDD b = bddFactory.ithVar(1);
        BDD c = bddFactory.ithVar(2);
        bddFactory.setVarOrder(new int[]{0, 1, 2});
        BDD formula = c.orWith(a.andWith(b));


        System.out.println(formula.forAll(formula).level());
    }
}
