package com.flowable.training.handson.design.interceptor;

import com.flowable.design.engine.api.ModelType;
import com.flowable.design.engine.api.history.AppRevision;
import com.flowable.design.engine.api.history.ModelHistory;
import com.flowable.design.engine.api.management.Workspace;
import com.flowable.design.engine.api.runtime.Model;
import com.flowable.design.rest.service.api.DesignRestApiInterceptor;
import com.flowable.design.rest.service.api.model.*;
import com.flowable.design.rest.service.api.model.export.ExportAppRequest;
import com.flowable.design.rest.service.api.model._import.ImportModelRequest;
import com.flowable.design.rest.service.api.model.clone.ModelDuplicateRequest;
import com.flowable.design.rest.service.api.model.history.AppRevisionCreateRequest;
import com.flowable.design.rest.service.api.model.history.ModelHistoryActionRequest;
import com.flowable.design.rest.service.api.model.translation.Translation;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public class BasicDesignRestApiInterceptor implements DesignRestApiInterceptor {

    public BasicDesignRestApiInterceptor() {

    }
    @Override
    public void updateApp(Workspace workspace, Model appModel, AppModelUpdateRequest request) {

    }

    @Override
    public void updateAppModels(Workspace workspace, Model appModel, AppModelUpdateModelsRequest request) {

    }

    @Override
    public void updateModelContent(Workspace workspace, Model appModel, Model model, ModelContentUpdateRequest request) {

    }

    @Override
    public void updateModelContent(Workspace workspace, Model model, ModelContentUpdateRequest request) {

    }

    @Override
    public void updateModel(Workspace workspace, Model appModel, Model model, ModelUpdateRequest request) {

    }

    @Override
    public void updateModel(Workspace workspace, Model model, ModelUpdateRequest request) {

    }

    @Override
    public void publishApp(Workspace workspace, Model appModel, AppPublishRequest request) {

    }

    @Override
    public void duplicateModel(Workspace workspace, Model model, ModelDuplicateRequest request) {

    }

    @Override
    public void duplicateModel(Workspace workspace, Model appModel, Model baseModel, ModelDuplicateRequest request) {

    }

    @Override
    public void deleteApp(Workspace workspace, Model appModel, AppDeleteRequest request) {

    }

    @Override
    public void deleteModel(Workspace workspace, Model model, DeleteModelInWorkspaceRequest request) {

    }

    @Override
    public void deleteModel(Workspace workspace, Model appModel, Model model, ModelDeleteRequest request) {

    }

    @Override
    public void createModel(Workspace workspace, ModelCreateRequest request) {

    }

    @Override
    public void createModel(Workspace workspace, Model appModel, ModelCreateRequest request) {

    }

    @Override
    public void migrateModel(Workspace workspace, Model model) {

    }

    @Override
    public void migrateModel(Workspace workspace, Model appModel, Model model) {

    }

    @Override
    public void importAppModel(Workspace workspace, ImportModelRequest restApiInterceptor) {

    }

    @Override
    public void importModel(Workspace workspace, ModelType modelType, ImportModelRequest request) {

    }

    @Override
    public void importModel(Workspace workspace, Model appModel, ModelType modelType, ImportModelRequest request) {

    }

    @Override
    public void importAndUpdateModel(Workspace workspace, Model model, ModelType modelType, ImportModelRequest request) {

    }

    @Override
    public void createAppRevision(Workspace workspace, Model appModel, AppRevisionCreateRequest request) {

    }

    @Override
    public void deleteAppRevision(Workspace workspace, Model appModel, AppRevision appRevision) {

    }

    @Override
    public void publishAppRevision(Workspace workspace, Model appModel, AppRevision appRevision, AppPublishRequest request) {

    }

    @Override
    public void revertAppToRevision(Workspace workspace, Model appModel, AppRevision appRevision) {

    }

    @Override
    public void copyRevisionIntoApp(Workspace workspace, Model app, AppRevision appRevision) {

    }

    @Override
    public void copyRevisionIntoWorkspace(Workspace workspace, AppRevision appRevision) {

    }

    @Override
    public void revertModelToVersion(Workspace workspace, Model appModel, Model model, ModelHistory modelHistory, ModelHistoryActionRequest request) {

    }

    @Override
    public void deleteHistoryModelsForModel(Workspace workspace, Model appModel, Model model) {

    }

    @Override
    public void deleteHistoryModelsForModel(Workspace workspace, Model model) {

    }

    @Override
    public void lockModel(Workspace workspace, Model model) {

    }

    @Override
    public void lockModel(Workspace workspace, Model appModel, Model model) {

    }

    @Override
    public void unlockModel(Workspace workspace, Model model, boolean force) {

    }

    @Override
    public void uploadDocumentTemplateResource(Workspace workspace, Model model) {

    }

    @Override
    public void uploadDocumentTemplateResource(Workspace workspace, Model appModel, Model model) {

    }

    @Override
    public void updateDocumentTemplateResource(Workspace workspace, Model appModel, Model model) {

    }

    @Override
    public void updateDocumentTemplateResource(Workspace workspace, Model model) {

    }

    @Override
    public void deleteTemplateResource(Workspace workspace, Model appModel, Model model) {

    }

    @Override
    public void deleteTemplateResource(Workspace workspace, Model model) {

    }

    @Override
    public void importAppTranslations(Workspace workspace, Model appModel, Map<String, Translation> translations) {

    }

    @Override
    public byte[] downloadAppRevision(Workspace workspace, Model model, AppRevision appRevision, byte[] byteArray) {
        return byteArray;
    }

    @Override
    public void importModels(Workspace workspace, Model model, List<MultipartFile> file, String something) {

    }

    @Override
    public void exportModel(Workspace workspace, Model model, Model newModel, int num) {

    }

    @Override
    public void exportModel(Workspace workspace, Model model, Model newModel) {

    }

    @Override
    public void exportApp(Workspace workspace, Model model, ExportAppRequest app) {
        
    }
}
