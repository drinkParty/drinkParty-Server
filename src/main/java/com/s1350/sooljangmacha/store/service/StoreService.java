package com.s1350.sooljangmacha.store.service;

import com.s1350.sooljangmacha.global.exception.BaseException;
import com.s1350.sooljangmacha.global.exception.BaseResponseCode;
import com.s1350.sooljangmacha.store.dto.request.PostStoreReq;
import com.s1350.sooljangmacha.store.entity.Store;
import com.s1350.sooljangmacha.store.repository.StoreImgRepository;
import com.s1350.sooljangmacha.store.repository.StoreLikeRepository;
import com.s1350.sooljangmacha.store.repository.StoreRepository;
import com.s1350.sooljangmacha.store.repository.StoreReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {
    private final StoreRepository storeRepository;
    private final StoreLikeRepository storeLikeRepository;
    private final StoreImgRepository storeImgRepository;
    private final StoreReviewRepository storeReviewRepository;


    // 위치별 포장마차 전체 조회

    // 포장마차 상세 조회

    // 포장마차 좋아요

    // 포장마차 등록

    @Transactional
    public void postStore(PostStoreReq req) {
        // 기획관련 한 번 더 물어보기 (예외처리)
        if(storeRepository.existsByXAndYAndIsEnable(req.getX(), req.getY(), true)) throw new BaseException(BaseResponseCode.EXISTS_STORE);
        // 저장
        storeRepository.save(Store.toEntity(req));
    }

    // 포장마차 후기 조회

    // 포장마차 후기 등록

}
