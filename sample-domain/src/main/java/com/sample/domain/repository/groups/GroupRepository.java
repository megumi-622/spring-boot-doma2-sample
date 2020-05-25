package com.sample.domain.repository.groups;

import static com.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sample.domain.dao.system.UploadFileDao;
import com.sample.domain.dao.groups.GroupDao;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.group.Group;
import com.sample.domain.dto.group.GroupCriteria;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseRepository;

import lombok.val;

@Repository
public class GroupRepository extends BaseRepository {

    @Autowired
    GroupDao groupDao;

    @Autowired
    UploadFileDao uploadFileDao;

    /**
     * グループを取得します。
     *
     * @param criteria
     * @param pageable
     * @return
     */
    public Page<Group> findAll(GroupCriteria criteria, Pageable pageable) {
        // ページングを指定する
        val options = createSelectOptions(pageable).count();
        val data = groupDao.selectAll(criteria, options, toList());
        return pageFactory.create(data, pageable, options.getCount());
    }

    /**
     * グループを取得します。
     *
     * @param criteria
     * @return
     */
    public Optional<Group> findOne(GroupCriteria criteria) {
        // 1件取得
        val group = groupDao.select(criteria);

        // 添付ファイルを取得する
        group.ifPresent(u -> {
            val uploadFileId = u.getUploadFileId();
            val uploadFile = ofNullable(uploadFileId).map(uploadFileDao::selectById);
            uploadFile.ifPresent((u::setUploadFile));
        });

        return group;
    }

    /**
     * グループを取得します。
     *
     * @return
     */
    public Group findById(final Long id) {
        return groupDao.selectById(id).orElseThrow(() -> new NoDataFoundException("group_id=" + id + " のデータがみつかりません。"));
    }

    /**
     * グループを追加します。
     *
     * @param inputGroup
     * @return
     */
    public Group create(final Group inputGroup) {
        // 1件登録
        groupDao.insert(inputGroup);

        return inputGroup;
    }

    /**
     * グループを更新します。
     *
     * @param inputGroup
     * @return
     */
    public Group update(final Group inputGroup) {

        val uploadFile = inputGroup.getUploadFile();
        if (uploadFile != null) {
            // 添付ファイルがある場合、登録、更新する
            val uploadFileId = inputGroup.getUploadFileId();
            if (uploadFileId == null) {
                uploadFileDao.insert(uploadFile);
            } else {
                uploadFileDao.update(uploadFile);
            }

            inputGroup.setUploadFileId(uploadFile.getId());
        }

        // 1件更新
        int updated = groupDao.update(inputGroup);

        if (updated < 1) {
            throw new NoDataFoundException("group_id=" + inputGroup.getId() + " のデータが見つかりません。");
        }

        return inputGroup;
    }

    /**
     * グループを論理削除します。
     *
     * @return
     */
    public Group delete(final Long id) {
        val group = groupDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("group_id=" + id + " のデータが見つかりません。"));

        int updated = groupDao.delete(group);

        if (updated < 1) {
            throw new NoDataFoundException("group_id=" + id + " は更新できませんでした。");
        }

        return group;
    }
}
