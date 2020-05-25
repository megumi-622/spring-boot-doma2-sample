package com.sample.web.admin.controller.html.groups.groups;

import static com.sample.domain.util.TypeUtils.toListType;
import static com.sample.web.base.WebConst.GLOBAL_MESSAGE;
import static com.sample.web.base.WebConst.MESSAGE_DELETED;

import java.util.AbstractCollection;
import java.util.List;

import com.sample.domain.service.groups.GroupService;
import com.sample.web.admin.controller.html.users.users.SearchUserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.UploadFile;
import com.sample.domain.dto.group.Group;
import com.sample.domain.dto.group.GroupCriteria;
import com.sample.domain.service.groups.GroupService;
import com.sample.web.base.controller.html.AbstractHtmlController;
import com.sample.web.base.util.MultipartFileUtils;
import com.sample.web.base.view.CsvView;
import com.sample.web.base.view.ExcelView;
import com.sample.web.base.view.PdfView;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * グループ管理
 */
@Controller
@RequestMapping("/groups/groups")
@SessionAttributes(types = { SearchGroupForm.class, GroupForm.class })
@Slf4j
public class GroupHtmlController extends AbstractHtmlController {

    @Autowired
    GroupService groupService;

    @ModelAttribute("groupForm")
    public GroupForm groupForm() { return new GroupForm(); }

    @ModelAttribute("searchGroupForm")
    public SearchGroupForm searchGroupForm() {
        return new SearchGroupForm();
    }

    @Override
    public String getFunctionName() { return "A_GROUP"; }

    /**
     * 登録画面 初期表示
     *
     * @param form
     * @oaram model
     * @return
     */
    @GetMapping("/new")
    public String newGroup(@ModelAttribute("groupForm") GroupForm form, Model model) {
        if (!form.isNew()) {
            // SessionAttributeに残っている場合は再生成する
            model.addAttribute("groupForm", new GroupForm());
        }

        return "modules/groups/groups/new";
    }

    /**
     * 登録処理
     *
     * @param form
     * @param br
     * @param attributes
     * @return
     */
    @PostMapping("/new")
    public String newGroup(@Validated @ModelAttribute("gorupForm") GroupForm form, BindingResult br,
            RedirectAttributes attributes ) {

        // 入力チェックエラーがある場合、元の画面に戻る
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/groups/groups/new";
        }

        // 入力値からDTOを作成する
        val inputGroup = modelMapper.map(form, Group.class);

        // 登録する
        val createGroup = groupService.create(inputGroup);

        return "redirect:/groups/groups/show/" + createGroup.getId();
    }

    /**
     * 一覧表示 初期表示
     *
     * @param model
     * @return
     */
    @GetMapping("/find")
    public String findGroup(@ModelAttribute SearchGroupForm form, Model model) {

        // 入力値を詰め替える
        val criteria = modelMapper.map(form, GroupCriteria.class);

        // 10件区切りで取得する
        val pages = groupService.findAll(criteria, form);

        // 画面に検索結果を渡す
        model.addAttribute("page", pages);

        return "modules/groups/groups/find";
    }

    /**
     * 検索結果
     *
     * @param form
     * @param br
     * @param attributes
     * @return
     */
    @PostMapping("/find")
    public String findGroup(@Validated @ModelAttribute("searchGroupForm") SearchGroupForm form, BindingResult br,
            RedirectAttributes attributes) {

        // 入力チェックエラーがある場合は、元の画面に戻る
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/groups/groups/find";
        }

        return "redirect:/groups/groups/find";
    }

    /**
     * 詳細画面
     *
     * @param groupId
     * @oaram model
     * @return
     */
    @GetMapping("/show/{groupId}")
    public String showGroup(@PathVariable Long groupId, Model model) {

        // 1件取得する
        val group = groupService.findById(groupId);
        model.addAttribute("group", group);

        if (group.getUploadFile() != null) {
            // 添付ファイルを取得する
            val uploadFile = group.getUploadFile();

            // Base64デコードして解答する
            val base64date = uploadFile.getContent().toBase64();
            val sb = new StringBuilder().append("data:image/png;base64, ").append(base64date);

            model.addAttribute("image", sb.toString());
        }

        return "modules/groups/groups/show";
    }

    /**
     * 編集画面 初期表示
     *
     * @param groupId
     * @param form
     * @param model
     * @return
     */
    @GetMapping("/edit/{groupId}")
    public String editGroup(@PathVariable Long groupId, @ModelAttribute("groupForm") GroupForm form, Model model) {

        // セッションから取得できる場合は、読み込み直さない
        if (!hasErrors(model)) {
            // 1件取得する
            val group = groupService.findById(groupId);

            // 取得したDtoをFormに詰め替える
            modelMapper.map(group, form);
        }

        return "modules/groups/groups/new";
    }

    /**
     * 編集画面 更新処理
     *
     * @param form
     * @param br
     * @param groupId
     * @param sessionStatus
     * @param attributes
     * @return
     */
    @PostMapping("/edit/{groupId}")
    public String editGroup(@Validated @ModelAttribute("groupForm") GroupForm form, BindingResult br,
            @PathVariable Long groupId, SessionStatus sessionStatus, RedirectAttributes attributes) {

        // 入力チェックエラーがある場合、元の画面に戻る
        if (br.hasErrors()) {
            setFlashAttributeErrors(attributes, br);
            return "redirect:/groups/groups/edit/" + groupId;
        }

        // 更新対象を取得する
        val group = groupService.findById(groupId);

        // 入力値を詰め替える
        modelMapper.map(form, group);

        val image = form.getGroupImage();
        if (image != null && !image.isEmpty()) {
            val uploadFile = new UploadFile();
            MultipartFileUtils.convert(image, uploadFile);
            group.setUploadFile(uploadFile);
        }

        // 更新する
        val updatedGroup = groupService.update(group);

        // セッションのgroupをクリアする
        sessionStatus.setComplete();

        return "redirect:/groups/groups/show/" + updatedGroup.getId();
    }

    /**
     * 削除処理
     *
     * @param groupId
     * @param attributes
     * @return
     */

    @PostMapping("remove/{groupId}")
    public String removeGroup(@PathVariable Long groupId, RedirectAttributes attributes) {

        // 論理削除する
        groupService.delete(groupId);

        // 削除成功メッセージ
        attributes.addAttribute(GLOBAL_MESSAGE, getMessage(MESSAGE_DELETED));

        return "redirect:/groups/groups/find";
    }

    /**
     * CSVをダウンロード
     *
     * @param filename
     * @return
     */
    @GetMapping("download/{filename:.+¥¥.csv}")
    public ModelAndView downloadCsv(@PathVariable String filename) {

        // 全件取得する
        val groups = groupService.findAll(new GroupCriteria(), Pageable.NO_LIMIT);

        // 詰め替える
        List<GroupCsv> csvList = modelMapper.map(groups.getData(), toListType(GroupCsv.class));

        // CSVスキーマクラス、データ、ダウンロード時のファイル名を取得する
        val view = new CsvView(GroupCsv.class, csvList, filename);

        return new ModelAndView(view);
    }

    /**
     * Excelダウンロード
     *
     * @param filename
     * @return
     */
    @GetMapping(path = "/download/{filename:.+¥¥.xlsx}")
    public ModelAndView downloadExcel(@PathVariable String filename) {

        // 全件取得する
        val groups = groupService.findAll(new GroupCriteria(), Pageable.NO_LIMIT);

        // Excelブック生成コールバック、データ、ダウンロード時のファイル名を指定する
        val view = new ExcelView(new GroupExcel(), groups.getData(), filename);

        return new ModelAndView(view);
    }

    /**
     * PDFダウンロード
     *
     * @param filename
     * @return
     */
    @GetMapping(path = "/download/{filename:.+¥¥.pdf}")
    public ModelAndView downloadPdf(@PathVariable String filename) {

        // 全件取得する
        val groups = groupService.findAll(new GroupCriteria(), Pageable.NO_LIMIT);

        // 帳票レイアウト、データ、ダウンロード時のフィイル名を指定する
        val view = new PdfView("reports/groups.jrxml", groups.getData(), filename);

        return new ModelAndView(view);
    }
}
