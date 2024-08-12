package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.storage.mpa.MPAStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MPAService {

    private final MPAStorage mpaDbStorage;

    public MPA getMPA(int id) {
        MPA mpa = mpaDbStorage.getMPAById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг MPA с id: " + id + " не найден."));
        log.info("Запрос по поиску рейтинга MPA обработан. Найден рейтинг MPA: {}.", mpa);
        return mpa;
    }

    public List<MPA> getAllMPA() {
        List<MPA> mpaRatings = mpaDbStorage.getAllMPA();
        log.info("Запрос на получение списка всех рейтингов MPA обработан. Найдены рейтинги MPA: {}", mpaRatings);
        return mpaRatings;
    }
}
