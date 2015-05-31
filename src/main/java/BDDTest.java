import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Kazako on 31.05.2015.
 */
public class BDDTest {

    BDDFactory bddFactory = BDDFactory.init(2, 2);

    public void quickTest() {
        bddFactory.setVarNum(3);
        BDD a = bddFactory.ithVar(0);
        BDD b = bddFactory.ithVar(1);
        BDD c = bddFactory.ithVar(2);

        BDD meh = a.or(b);
        BDD t = meh.and(c);

        List uh = t.allsat();
        Iterator r = uh.iterator();

        byte[] eg = (byte[]) r.next();
        byte[] eg1 = (byte[]) r.next();

        System.out.println(eg[0] + " " + eg[1] + " " + eg[2]);
        System.out.println(eg1[0] + " " + eg1[1] + " " + eg1[2]);

        System.out.println(t.satCount());

    }
}
