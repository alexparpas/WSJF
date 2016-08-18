package com.example.alexparpas.wsjf.model;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.alexparpas.wsjf.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Alex on 07/08/2016.
 */
public class JobLab {
    private static JobLab sJobLab;
    private List<Job> mJobs;
//    private String mockDescription = "Has eu quem deterruisset. Mazim civibus partiendo id vis. Cu debitis veritus accommodare pri. Ut nam autem melius omnium. Elitr quaeque no nam, ne meis honestatis est, sed ut legimus conclusionemque.\n" +
//            "Porro posidonium no cum, eum id paulo corpora conceptam. In omnes dissentias theophrastus est, te sed labitur placerat singulis. Oportere principes at mei, partem voluptaria eam ut. Sea erant veniam ad, ut deserunt eleifend pri. No nisl tota inimicus nec, cu adolescens temporibus pro, duo cu affert discere gloriatur. Ei everti invenire quaestio pri, ex impetus maluisset per.\n" +
//            "No oblique apeirian hendrerit per, mea enim offendit et. Mel clita impetus consequat et, pro porro sonet partiendo ei. Ad mucius essent conclusionemque ius. Modus interesset pro eu, euripidis consectetuer te est. Vel te quodsi latine debitis, facer quodsi electram no nec, has vitae evertitur disputando ad.\n" +
//            "Diam libris vis ne, diam error assentior an quo. Mea et graece vocibus pericula, wisi natum dicta pri ne. Errem clita ubique eos eu, eam magna clita ut. Ne vix ignota dolorem voluptatibus, vis iisque ancillae no, vel omnis autem cu.\n" +
//            "Percipit omittantur usu an, nec solet patrioque no. Sit nisl atqui eleifend id. Ea tation exerci reprehendunt sit. Suavitate reprimique vis at, eu sea suscipit molestiae. Id tacimates voluptatum per, ne his percipit electram quaerendum.\n" +
//            "No sed idque sensibus, an audiam probatus ius. Simul necessitatibus has in, ea debet corpora nam. Sit ne legere causae denique, omittam offendit probatus ut vel. Noster suscipiantur nam ex. Id dicunt maiorum disputationi quo, ius diam urbanitas te, an malis dolorem definiebas ius. Ex justo gubergren his, te vis rebum equidem. Ne veniam audire eripuit vim, eam affert accumsan adipiscing ut.\n" +
//            "Vis dolor corpora et, et vix laudem tempor, eu ius case feugait. Populo praesent pericula nam cu, augue bonorum accusamus eu vel. Paulo mundi in qui, epicuri intellegat ei quo. Sea cu facete definitionem, legendos disputando vix ut. At verear efficiendi concludaturque vix, ne eum facilis singulis, utroque inimicus reprehendunt qui cu.\n" +
//            "Id modus sonet sit, nobis eloquentiam eum in. Meliore detracto definitiones et his, fugit numquam adolescens mea at. Nam amet congue forensibus ei, ut tibique aliquando voluptatum mel. Pri ei definitionem concludaturque. Vidit vivendo consectetuer ne cum, mel sale tibique ne, eu illud mediocritatem pri. Et eam putent numquam, graeci dictas virtute ea ius, usu te dicat dicit recusabo.\n" +
//            "Has ad errem scaevola, eu vis dicat facilisi constituam. Ad usu quando minimum, cum quando noluisse et. Sit ex mucius graecis. Ferri dolores dissentiunt ea duo, ea vim tale aeterno pertinacia. Utroque perpetua adversarium vim ei.\n" +
//            "Nonumes accusata ut nam. Ea his exerci consul phaedrum, his amet nisl eu. Ei quod modus aeterno per, ex viris latine has, et pro soleat persius postulant. Et nam sale fastidii, mea quis suas nobis ne.";

    private JobLab(Context context) {
        mJobs = new ArrayList<>();
//        mockData();
    }

    public static JobLab get(Context context) {
        if (sJobLab == null) {
            sJobLab = new JobLab(context);
        }
        return sJobLab;
    }

//    private void mockData(){
//
//        for (int i = 0; i < 20; i++) {
//            Job job = new Job();
//            job.setJobName("Job #" + i);
//            job.setCompleted(i % 2 == 0); //Every other one
//            job.setJobDescription(mockDescription);
//
//            //Populate the list with 100 mock jobs
//            Random rn = new Random();
//            int min = 1;
//            int max = 13;
//
//            for (int j = 0; i < 4; i++) {
//                int randomValue = rn.nextInt(max - min + 1) + min;
//                switch (j) {
//                    case 0:
//                        job.setUserValue(randomValue);
//                        break;
//                    case 1:
//                        job.setTimeValue(randomValue);
//                        break;
//                    case 2:
//                        job.setRroeValue(randomValue);
//                        break;
//                    case 3:
//                        job.setJobSize(randomValue);
//                }
//            }
//            mJobs.add(job);
//        }
//    }

    public void addJob(Job j){
        mJobs.add(j);
    }

    public List<Job> getJobs() {
        return mJobs;
    }

    public Job getJob(UUID id){
        for(Job job: mJobs){
            if(job.getId().equals(id)){
                return job;
            }
        }
        return null;
    }
}
