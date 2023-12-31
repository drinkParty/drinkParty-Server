package com.s1350.sooljangmacha.store.service;

import com.s1350.sooljangmacha.global.exception.BaseException;
import com.s1350.sooljangmacha.global.exception.BaseResponseCode;
import com.s1350.sooljangmacha.store.dto.request.PostStoreReq;
import com.s1350.sooljangmacha.store.dto.request.PostStoreReviewReq;
import com.s1350.sooljangmacha.store.dto.response.GetStoreListRes;
import com.s1350.sooljangmacha.store.dto.response.GetStoreRes;
import com.s1350.sooljangmacha.store.dto.response.GetStoreReviewRes;
import com.s1350.sooljangmacha.store.entity.Store;
import com.s1350.sooljangmacha.store.entity.StoreImg;
import com.s1350.sooljangmacha.store.entity.StoreLike;
import com.s1350.sooljangmacha.store.entity.StoreReview;
import com.s1350.sooljangmacha.store.repository.StoreImgRepository;
import com.s1350.sooljangmacha.store.repository.StoreLikeRepository;
import com.s1350.sooljangmacha.store.repository.StoreRepository;
import com.s1350.sooljangmacha.store.repository.StoreReviewRepository;
import com.s1350.sooljangmacha.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.s1350.sooljangmacha.global.Constants.*;
import static com.s1350.sooljangmacha.global.exception.BaseResponseCode.REQUEST_VALIDATION;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {
    private final StoreRepository storeRepository;
    private final StoreLikeRepository storeLikeRepository;
    private final StoreImgRepository storeImgRepository;
    private final StoreReviewRepository storeReviewRepository;

    // 위치별 포장마차 전체 조회
    public List<GetStoreListRes> getStores(User user, String category) {
        Stream<GetStoreListRes> getStoreListResStream = storeRepository.findAllByIsEnable(true).stream()
                .map(store -> GetStoreListRes.toDto(store, storeLikeRepository.getLikeCountByIsEnable(store), storeLikeRepository.existsByUserAndStoreAndIsEnable(user, store, true)));
        return sortStoreList(category, getStoreListResStream);
    }

    public List<GetStoreListRes> sortStoreList(String category, Stream<GetStoreListRes> getStoreListResStream) {
        switch (category) {
            case REVIEW:
                return getStoreListResStream
                        .sorted((o1, o2) -> o2.getReviewCount().compareTo(o1.getReviewCount()))
                        .collect(Collectors.toList());
            case LIKE:
                return getStoreListResStream
                        .sorted((o1, o2) -> o2.getLikeCount().compareTo(o1.getLikeCount()))
                        .collect(Collectors.toList());
            case DESC:
                return getStoreListResStream
                        .collect(Collectors.toList());
            default:
                throw new BaseException(REQUEST_VALIDATION);
        }
    }

    // 포장마차 상세 조회
    public GetStoreRes getStore(User user, Long storeId) {
        Store store = storeRepository.findByIdAndIsEnable(storeId, true).orElseThrow(() -> new BaseException(BaseResponseCode.STORE_NOT_FOUND));
        return GetStoreRes.toDto(store, storeLikeRepository.getLikeCountByIsEnable(store), storeLikeRepository.existsByUserAndStoreAndIsEnable(user, store, true));
    }

    // 포장마차 좋아요
    @Transactional
    public void postStoreLike(User user, Long storeId) {
        Store store = storeRepository.findByIdAndIsEnable(storeId, true).orElseThrow(() -> new BaseException(BaseResponseCode.STORE_NOT_FOUND));
        storeLikeRepository.findByUserAndStore(user, store)
                .ifPresentOrElse(
                        sl -> {
                           sl.changeValue();
                           storeLikeRepository.save(sl);
                        },
                        () -> storeLikeRepository.save(StoreLike.toEntity(user, store))
                );
    }

    // 포장마차 등록
    @Transactional
    public void postStore(User user, PostStoreReq req) {
        // 기획관련 한 번 더 물어보기 (예외처리)
        if(storeRepository.existsByXAndYAndIsEnable(req.getX(), req.getY(), true)) throw new BaseException(BaseResponseCode.EXISTS_STORE);
        // 저장
        Store store = Store.toEntity(user, req);
        storeRepository.save(store);
        storeImgRepository.saveAll(StoreImg.toEntityList(req.getImgUrls(), store));
    }

    // 포장마차 후기 조회
    public List<GetStoreReviewRes> getStoreReview(Long storeId) {
        Store store = storeRepository.findByIdAndIsEnable(storeId, true).orElseThrow(() -> new BaseException(BaseResponseCode.STORE_NOT_FOUND));
        return store.getStoreReviewList().stream()
                .map(GetStoreReviewRes::toDto)
                .collect(Collectors.toList());
    }

    // 포장마차 후기 등록
    @Transactional
    public void postStoreReview(User user, Long storeId, @Valid PostStoreReviewReq req) {
        Store store = storeRepository.findByIdAndIsEnable(storeId, true).orElseThrow(() -> new BaseException(BaseResponseCode.STORE_NOT_FOUND));
        storeReviewRepository.save(StoreReview.toEntity(user, store, req.getContent()));
    }
}
