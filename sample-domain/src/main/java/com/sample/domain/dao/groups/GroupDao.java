package com.sample.domain.dao.groups;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import com.sample.domain.dto.user.User;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.group.Group;
import com.sample.domain.dto.group.GroupCriteria;

@ConfigAutowireable
@Dao
public interface GroupDao {

    /**
     * グループを取得します。
     * @param criteria
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final GroupCriteria criteria, final SelectOptions options, final Collector<Group, ?, R> collector);

    /**
     * グループを1件取得します。
     * @param id
     * @return
     */
    @Select
    Optional<Group> selectById(Long id);

    /**
     * グループを1件取得します。
     * @param criteria
     * @return
     */
    @Select
    Optional<Group> select(GroupCriteria criteria);

    /**
     * グループを登録します。
     * @param group
     * @return
     */
    @Insert
    int insert(Group group);

    /**
     * グループを更新します。
     *
     * @param group
     * @return
     */
    @Update
    int update(Group group);

    /**
     * グループを論理削除します。
     *
     * @param group
     * @return
     */
    @Update(excludeNull = true) // NULLの項目は更新対象にしない
    int delete(Group group);

    /**
     * グループを一括登録します。
     *
     * @param groups
     * @return
     */
    @BatchInsert
    int[] insert(List<Group> groups);

    /**
     * グループを一括更新します。
     *
     * @param users
     * @return
     */
    @BatchUpdate
    int[] update(List<User> users);
}

