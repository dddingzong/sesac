package project.sesac.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.sesac.domain.Information;
import project.sesac.domain.type.InformationPreference;
import project.sesac.domain.type.InformationType;
import project.sesac.repository.InformationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InformationService {

    private final InformationRepository informationRepository;

    @Transactional
    public List<Information> findByPreference(InformationPreference preference){
        if (preference == InformationPreference.ALL) {
            return informationRepository.findAll();
        }
        return informationRepository.findByInformationType(
                preference == InformationPreference.CARE ? InformationType.CARE : InformationType.JOB
        );
    }

    @Transactional
    public List<Information> getCareInfo(){
        return informationRepository.findByInformationType(InformationType.CARE);
    }

    @Transactional
    public List<Information> getJobInfo(){
        return informationRepository.findByInformationType(InformationType.JOB);
    }

    @Transactional
    public List<Information> getSearchInfo(String keyword){
        return informationRepository.getSearchInfo(keyword);
    }

    public List<Information> findAll(){
        return informationRepository.findAll();
    }

    @Transactional
    public List<Information> keywordFilter(List<Information> informationList, String keyword) {
        List<Information> realList = new ArrayList<>();

        for (int i=0;i<informationList.size();i++) {
            Information information = informationList.get(i);
            if (information.getTitle().contains(keyword)){
                realList.add(informationList.get(i));
            }
        }
        return realList;
    }

    public void shuffleLogic(List<Information> informationList) {

        ArrayList<Information> careInformationList = new ArrayList<>();
        ArrayList<Information> jobInformationList = new ArrayList<>();

        for(int i=0; i<informationList.size();i++) {
            Information information = informationList.get(i);
            if (information.getInformationType() == InformationType.CARE){ // 복지
                careInformationList.add(information);
            } else if(information.getInformationType() == InformationType.JOB) { // 취업
                jobInformationList.add(information);
            }
        }

        for (int i = informationList.size()-1; i >= 0; i--) {
            informationList.remove(i);
        }

        int smallListSize = 0;
        if (careInformationList.size()==jobInformationList.size()){
            smallListSize = careInformationList.size();
        } else {
            smallListSize = Math.min(jobInformationList.size(), careInformationList.size());
        }

        for (int i=0; i<smallListSize; i++){
            informationList.add(careInformationList.get(i));
            informationList.add(jobInformationList.get(i));
        }

        if (careInformationList.size() > jobInformationList.size()){
            for (int i = smallListSize; i<careInformationList.size();i++){
                informationList.add(careInformationList.get(i));
            }
        } else {
            for (int i = smallListSize; i<jobInformationList.size();i++){
                informationList.add(jobInformationList.get(i));
            }
        }

    }


}
