/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.spm.api;

import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.organisation.staff.domain.Staff;
import org.mifosplatform.organisation.staff.domain.StaffRepository;
import org.mifosplatform.organisation.staff.exception.StaffNotFoundException;
import org.mifosplatform.portfolio.client.domain.Client;
import org.mifosplatform.portfolio.client.domain.ClientRepository;
import org.mifosplatform.portfolio.client.exception.ClientNotFoundException;
import org.mifosplatform.spm.data.ScorecardData;
import org.mifosplatform.spm.domain.Scorecard;
import org.mifosplatform.spm.domain.Survey;
import org.mifosplatform.spm.exception.SurveyNotFoundException;
import org.mifosplatform.spm.service.ScorecardService;
import org.mifosplatform.spm.service.SpmService;
import org.mifosplatform.spm.util.ScorecardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/surveys/{surveyId}/scorecards")
@Component
@Scope("singleton")
public class ScorecardApiResource {

    private final PlatformSecurityContext securityContext;
    private final SpmService spmService;
    private final ScorecardService scorecardService;
    private final StaffRepository staffRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public ScorecardApiResource(final PlatformSecurityContext securityContext, final SpmService spmService,
                                final ScorecardService scorecardService, final StaffRepository staffRepository,
                                final ClientRepository clientRepository) {
        super();
        this.securityContext = securityContext;
        this.spmService = spmService;
        this.scorecardService = scorecardService;
        this.staffRepository = staffRepository;
        this.clientRepository = clientRepository;
    }

    @GET
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public List<ScorecardData> findBySurvey(@PathParam("surveyId") final Long surveyId) {
        this.securityContext.authenticatedUser();

        final Survey survey = findSurvey(surveyId);

        final List<ScorecardData> result = new ArrayList<>();

        final List<Scorecard> scorecards = this.scorecardService.findBySurvey(survey);

        if (scorecards == null) {
            for (final Scorecard scorecard : scorecards) {
                result.add(ScorecardMapper.map(scorecard));
            }
        }

        return result;
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public void createScorecard(@PathParam("surveyId") final Long surveyId, final ScorecardData scorecardData) {
        this.securityContext.authenticatedUser();

        final Survey survey = findSurvey(surveyId);

        final Staff staff = this.staffRepository.findOne(scorecardData.getStaffId());

        if (staff == null) {
            throw new StaffNotFoundException(scorecardData.getStaffId());
        }

        final Client client = this.clientRepository.findOne(scorecardData.getClientId());

        if (client == null) {
            throw new ClientNotFoundException(scorecardData.getClientId());
        }

        this.scorecardService.createScorecard(ScorecardMapper.map(scorecardData, survey, staff, client));
    }

    private Survey findSurvey(final Long surveyId) {
        final Survey survey = this.spmService.findById(surveyId);
        if (survey == null) {
            throw new SurveyNotFoundException(surveyId);
        }
        return survey;
    }
}
