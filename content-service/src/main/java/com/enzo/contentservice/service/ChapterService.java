    package com.enzo.contentservice.service;

    import com.enzo.contentservice.dto.ChapterDTO;
    import com.enzo.contentservice.dto.ChapterSummaryDTO;
    import com.enzo.contentservice.service.proxy.ChapterFeignClient;
    import lombok.AllArgsConstructor;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    @AllArgsConstructor
    public class ChapterService {
        private final ChapterFeignClient chapterFeignClient;

        public ChapterDTO create(ChapterDTO chapterDTO) {
            return chapterFeignClient.save(chapterDTO);
        }

        public List<ChapterDTO> findAll() {
            return  chapterFeignClient.getAllChapter();
        }

        public List<ChapterSummaryDTO> findAllSummary() {
            return chapterFeignClient.getChapterSummaries();
        }

        public ChapterDTO findById(Long id) {
            return chapterFeignClient.getChapterById(id);
        }

        public ChapterDTO updateChapter(Long id, ChapterDTO chapterDTO) {
            return chapterFeignClient.updateChapter(id, chapterDTO);
        }

        public void delete(Long id) {chapterFeignClient.deleteChapter(id);}
    }
