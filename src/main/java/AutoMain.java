import com.oggu.auto.core.common.CommonConstants;
import com.oggu.auto.core.exec.PrepareTests;
import com.oggu.auto.core.exec.TestRunner;

/**
 * 
 */

/**
 * @author Bhaskar
 *
 */
public class AutoMain implements CommonConstants {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String uuid = RANDOM_UUID;

		PrepareTests.prepareTests(uuid);
		TestRunner.runTests(uuid);

	}

}
