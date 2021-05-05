/*
 * Copyright 2020 Johnny850807 (Waterball) 潘冠辰
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *       http://www.apache.org/licenses/LICENSE-2.0
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package tw.waterball.judgegirl.problem.domain.repositories;

import tw.waterball.judgegirl.commons.models.files.FileResource;
import tw.waterball.judgegirl.primitives.problem.LanguageEnv;
import tw.waterball.judgegirl.primitives.problem.Problem;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public interface ProblemRepository {

    Optional<Problem> findProblemById(int problemId);

    Optional<FileResource> downloadProvidedCodes(int problemId, String languageEnvName);

    List<Problem> find(ProblemQueryParams params);

    default List<Problem> findAll() {
        return find(ProblemQueryParams.NO_PARAMS);
    }

    int getPageSize();

    List<String> getTags();

    Problem save(Problem problem, Map<LanguageEnv, InputStream> providedCodesZipMap, InputStream testcaseIOsZip);

    Problem save(Problem problem);

    int saveProblemWithTitleAndGetId(String title);

    void patchProblem(int problemId, PatchProblemParams params);

    boolean problemExists(int problemId);

    List<Problem> findProblemsByIds(int... problemIds);

    void archiveProblemById(int problemId);

    void deleteProblemById(int problemId);

    void deleteAll();

    void saveTags(List<String> tagList);
}