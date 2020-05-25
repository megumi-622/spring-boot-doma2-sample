package com.sample.domain.service.groups;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.group.Group;
import com.sample.domain.dto.group.GroupCriteria;
import com.sample.domain.repository.groups.GroupRepository;
import com.sample.domain.service.BaseTransactionalService;

/**
 * グループサービス
 */
@Service
public class GroupService {

    @Autowired
    GroupRepository groupRepository;

    /**
     * グループを複数取得します。
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true) // 読み取りのみの場合は指定する
    public Page<Group> findAll(GroupCriteria criteria, Pageable pageable) {
        Assert.notNull(criteria, "criteria must not be null");
        return groupRepository.findAll(criteria, pageable);
    }

    /**
     * グループを取得します
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<Group> findOne(GroupCriteria criteria) {
        Assert.notNull(criteria, "criteria must not be null");
        return groupRepository.findOne(criteria);
    }

    /**
     * グループを取得します。
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Group findById(final Long id) {
        Assert.notNull(id, "id must not be null");
        return groupRepository.findById(id);
    }

    /**
     * グループを追加します
     *
     * @param inputGroup
     * @return
     */
    public Group create(final Group inputGroup) {
        Assert.notNull(inputGroup, "inputGroup must not be null");
        return groupRepository.create(inputGroup);
    }

    /**
     * グループを更新します
     *
     * @param inputGroup
     * @return
     */
    public Group update(final Group inputGroup) {
        Assert.notNull(inputGroup, "inputGroup mest not be null");
        return groupRepository.update(inputGroup);
    }

    /**
     * グループを論理削除します。
     *
     * @return
     */
    public Group delete(final Long id) {
        Assert.notNull(id, "id must not be null");
        return groupRepository.delete(id);
    }
}
