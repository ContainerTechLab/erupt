package xyz.erupt.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Service;
import xyz.erupt.core.annotation.EruptDataSource;
import xyz.erupt.core.config.EruptProp;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author liyuepeng
 * @date 2020-01-13
 */
@Service
public class EntityManagerService implements ApplicationRunner {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EruptProp eruptProp;

    private Map<String, EntityManagerFactory> entityManagerMap;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (null != eruptProp.getDbs()) {
            entityManagerMap = new HashMap<>();
            for (EruptProp.DB prop : eruptProp.getDbs()) {
                LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
                {
                    JpaProperties jpa = prop.getJpa();
                    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
                    vendorAdapter.setGenerateDdl(jpa.isGenerateDdl());
                    vendorAdapter.setDatabase(jpa.getDatabase());
                    vendorAdapter.setShowSql(jpa.isShowSql());
                    vendorAdapter.setDatabasePlatform(jpa.getDatabasePlatform());
                    factory.setJpaVendorAdapter(vendorAdapter);
                }
                {
                    factory.setDataSource(prop.getDatasource().initializeDataSourceBuilder().build());
                    factory.setPackagesToScan(EruptApplication.getScanPackage());
                    factory.afterPropertiesSet();
                }
                entityManagerMap.put(prop.getDatasource().getName(), factory.getObject());
            }
        }
    }


    /**
     * 如果使用了@EruptDataSource多数据源，调用此方法必须手动关闭, close()
     *
     * @param eruptClass
     * @return
     */
    public EntityManager getEntityManager(Class<?> eruptClass) {
        EruptDataSource eruptDataSource = eruptClass.getAnnotation(EruptDataSource.class);
        if (null == eruptDataSource) {
            return entityManager;
        } else {
            return entityManagerMap.get(eruptDataSource.value()).createEntityManager();
        }
    }


    public void getEntityManager(Class<?> eruptClass, Consumer<EntityManager> consumer) {
        EruptDataSource eruptDataSource = eruptClass.getAnnotation(EruptDataSource.class);
        if (null == eruptDataSource) {
            consumer.accept(entityManager);
        } else {
            EntityManager em = entityManagerMap.get(eruptDataSource.value()).createEntityManager();
            try {
                em.getTransaction().begin();
                consumer.accept(em);
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
            } finally {
                if (em.isOpen()) {
                    em.close();
                }
            }
        }
    }


}
