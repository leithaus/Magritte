/**
 * Created by IntelliJ IDEA.
 * User: christopherrude
 * Date: Apr 20, 2008
 * Time: 10:39:42 AM
 * To change this template use File | Settings | File Templates.
 */
package org.openquark.gems.client.rails;

import org.openquark.cal.services.MetaModule;
import org.openquark.cal.services.GemEntity;
import org.openquark.cal.compiler.CALDocComment;
import org.openquark.cal.compiler.FunctionalAgent;
import org.openquark.cal.compiler.TypeExpr;
import org.openquark.cal.metadata.FunctionalAgentMetadata;
import org.openquark.gems.client.navigator.NavHtmlFactory;
import org.openquark.gems.client.GemCutter;
import org.hibernate.classic.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Vector;
import java.util.Set;
import java.util.HashSet;
import java.sql.Timestamp;

public class GemDumper {
    private static GemDumper ourInstance = new GemDumper();
    private static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    public static GemDumper getInstance() {
        return ourInstance;
    }

    private GemDumper() {
    }

    public void addGems(List<MetaModule> modules) {
        for (MetaModule module : modules) {
            System.out.print("on module: ");
            System.out.println(module.getName().toString());

//                        System.out.print("module name source: ");
//                        System.out.println(module.getName().toSourceText());

//            module.getName().toSourceText();

            RailsCategoriesEntity category = addCategory(module.getName().toString());

            // set source text on category
            //
            setSourceText(category, module.getName().toSourceText());

            int numGems = module.getNGemEntities();
            for (int i = 0; i < numGems; ++i) {
                GemEntity gem = module.getNthGemEntity(i);

                FunctionalAgent functionalAgent = gem.getFunctionalAgent();

                FunctionalAgentMetadata metadata = gem.getMetadata(GemCutter.getLocaleFromPreferences());
                if (metadata.isHidden() || metadata.isExpert()) {
                    // skip these
                    //
                    continue;
                }

                CALDocComment calHelp = functionalAgent.getCALDocComment();
                String help = NavHtmlFactory.getHtmlForCALDocSummaryWithNoHyperlinks(calHelp,
                        GemCutter.getLocaleFromPreferences(),
                        false);

//                gem.getMetadata(GemCutter.getLocaleFromPreferences()).getArguments()[0].copy()

                String type = trunc(gem.getName().getQualifiedName());
                String name = gem.getName().getUnqualifiedName();

                System.out.print("\tgem: ");
                System.out.print(name);
                System.out.print("  (");
                System.out.print(type);
                System.out.println(")");


                System.out.print("\thelp:");
                System.out.println(help);

                String returnResultType = functionalAgent.getTypeExpr().getResultType().toString();

                System.out.print("\t\treturn type:");
                System.out.println(returnResultType);


                long nowMS = System.currentTimeMillis();
                Timestamp nowTimestamp = new Timestamp(nowMS);

                Set<RailsTerminalsEntity> inTerminals = new HashSet();

                if (null != functionalAgent.getTypeExpr().getArgumentType()) {

                    TypeExpr[] argTypes = functionalAgent.getTypeExpr().getArgumentType().getTypePieces();
                    for (int argIdx = 0; argIdx < argTypes.length; argIdx++) {
                        TypeExpr argType = argTypes[argIdx];
                        String argName = "_INPUT";

                        if (argIdx < functionalAgent.getNArgumentNames()) {
                            argName = functionalAgent.getArgumentName(argIdx);
                        }

                        System.out.print("\t\targ:");
                        System.out.print(argName);
                        System.out.print(" (type: ");
                        System.out.print(argType.toString());
                        System.out.println(")");

                        RailsTerminalsEntity inTerminal = createTerminal(argName, argType.toString());

                        inTerminals.add(inTerminal);

//                    findOrCreateTerminal()

                    }
                }

                Set<RailsTerminalsEntity> outTerminals = new HashSet();
                {
                    // note: looks like there's only one out terminal?
                    //
                    RailsTerminalsEntity outTerminal = createTerminal("_OUTPUT", returnResultType);

                    outTerminals.add(outTerminal);
                }


                System.out.println( "creating rails module" );
                RailsPipeModulesEntity railsModule = addModule(type);

                railsModule.setCategory(category);

                railsModule.setName(name);
                railsModule.setHelp(help);
                railsModule.setType(type);
                railsModule.setDescription(gem.getName().getQualifiedName());

                railsModule.setIn_terminals(inTerminals);
                railsModule.setOut_terminals(outTerminals);

                System.out.println( "creating rails module" );

                updateModule(railsModule);
                updateModuleCategory(railsModule);                
                
            }

        }

    }

    private String trunc(String type) {
        if (type.length() > 255)
        {
            type = type.substring(0, 255);
        }

        return type.replaceAll("\\.", "");
    }

    private RailsTerminalsEntity createTerminal(String argName, String argType) {
        RailsTerminalsEntity terminal = new RailsTerminalsEntity();
        terminal.setName(argName);
        terminal.setType(trunc(argType));

        long nowMS = System.currentTimeMillis();
        Timestamp nowTimestamp = new Timestamp(nowMS);

        terminal.setCreatedAt(nowTimestamp);
        terminal.setUpdatedAt(nowTimestamp);

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        session.save(terminal);

        session.getTransaction().commit();

        return terminal;
    }

    private void saveTerminal(RailsTerminalsEntity terminal) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        session.update(terminal);

        session.getTransaction().commit();
    }

    private void setSourceText(RailsCategoriesEntity category, String sourceText) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        category.setSource_text(sourceText);
        session.update(category);

        session.getTransaction().commit();
    }

    public RailsCategoriesEntity addCategory(String categoryName) {
        Session session = sessionFactory.getCurrentSession();

        session.beginTransaction();

        RailsCategoriesEntity category;

        // try and find an existing category
        //
        category = getCategory(categoryName);
        if (null == category) {
            // no existing one was found, add a new one
            //
            category = new RailsCategoriesEntity();

            category.setName(categoryName);

            long nowMS = System.currentTimeMillis();
            Timestamp nowTimestamp = new Timestamp(nowMS);

            category.setCreatedAt(nowTimestamp);
            category.setUpdatedAt(nowTimestamp);

            session.save(category);
        }

        session.getTransaction().commit();

        return category;
    }

    public RailsCategoriesEntity getCategory(String categoryName) {
        Session session = sessionFactory.getCurrentSession();

        List results =
                session.createCriteria(RailsCategoriesEntity.class)
                        .add(Restrictions.eq("name", categoryName))
                        .setMaxResults(1)
                        .list();

        if (0 == results.size()) {
            return null;
        } else {
            return (RailsCategoriesEntity) results.get(0);
        }
    }


    public RailsPipeModulesEntity addModule(String type) {
        Session session = sessionFactory.getCurrentSession();

        session.beginTransaction();

        RailsPipeModulesEntity module;

        // try and find an existing category
        //
        module = getModule(type);
        if (null == module) {
            // no existing one was found, add a new one
            //
            module = new RailsPipeModulesEntity();

            module.setType(type);

            long nowMS = System.currentTimeMillis();
            Timestamp nowTimestamp = new Timestamp(nowMS);

            module.setCreatedAt(nowTimestamp);
            module.setUpdatedAt(nowTimestamp);

            session.save(module);
        }

        session.getTransaction().commit();

        return module;
    }



    public RailsPipeModulesEntity getModule(String type) {
        Session session = sessionFactory.getCurrentSession();

        List results =
                session.createCriteria(RailsPipeModulesEntity.class)
                        .add(Restrictions.eq("type", type))
                        .setMaxResults(1)
                        .list();

        if (0 == results.size()) {
            return null;
        } else {
            return (RailsPipeModulesEntity) results.get(0);
        }
    }


    public RailsModuleCategoriesEntity getModuleCategory(Integer moduleCategoryId) {
        Session session = sessionFactory.getCurrentSession();

        List results =
                session.createCriteria(RailsModuleCategoriesEntity.class)
                        .add(Restrictions.eq("id", moduleCategoryId))
                        .setMaxResults(1)
                        .list();

        if (0 == results.size()) {
            return null;
        } else {
            return (RailsModuleCategoriesEntity) results.get(0);
        }
    }

    private void updateModuleCategory(RailsPipeModulesEntity module) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        // LGM -- added to address problem with module_category join
        RailsModuleCategoriesEntity moduleCategory =
                getModuleCategory( module.getId() );
        Boolean moduleCategoryExists = moduleCategory != null;

        if (!moduleCategoryExists)
        {
            moduleCategory = new RailsModuleCategoriesEntity();

            moduleCategory.setId(module.getId());
            moduleCategory.setPipeModuleId(module.getId());
            moduleCategory.setCategoryId(module.getCategory().getId());

            long nowMS = System.currentTimeMillis();
            Timestamp nowTimestamp = new Timestamp(nowMS);

            moduleCategory.setCreatedAt(nowTimestamp);
            moduleCategory.setUpdatedAt(nowTimestamp);

            session.save(moduleCategory);
        }
        else {
            moduleCategory.setId(module.getId());
            moduleCategory.setPipeModuleId(module.getId());
            moduleCategory.setCategoryId(module.getCategory().getId());

            long nowMS = System.currentTimeMillis();
            Timestamp nowTimestamp = new Timestamp(nowMS);

            moduleCategory.setUpdatedAt(nowTimestamp);

            session.update(moduleCategory);
        }
        // LGM -- end addition

        session.getTransaction().commit();
    }


    private void updateModule(RailsPipeModulesEntity module) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        session.update(module);

        session.getTransaction().commit();
    }

}
