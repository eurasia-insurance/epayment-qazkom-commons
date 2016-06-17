package test.com.lapsa.kkb.services.impl;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.security.cert.X509Certificate;

import javax.ejb.EJB;
import javax.naming.NamingException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.lapsa.kkb.api.KKBMerchantSignatureService;
import com.lapsa.kkb.services.impl.DefaultKKBMerchantSignatureService;

@RunWith(Arquillian.class)
public class TestCase {

    @Deployment
    public static JavaArchive createDeployment() {
	JavaArchive jar = ShrinkWrap.create(JavaArchive.class);
	jar.addPackages(true, DefaultKKBMerchantSignatureService.class.getPackage());
	System.out.println(jar.toString(true));
	return jar;
    }

    @EJB
    private KKBMerchantSignatureService merchantSignatureService;

    @Test
    public void testMerchantSignature() throws NamingException {
	assertThat(merchantSignatureService, not(nullValue()));
	X509Certificate cert = merchantSignatureService.getSignatureCertificate();
	assertThat(cert, not(nullValue()));
    }

}
