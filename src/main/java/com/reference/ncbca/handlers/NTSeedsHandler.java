package com.reference.ncbca.handlers;

import com.reference.ncbca.dao.NTSeedsDao;
import com.reference.ncbca.model.NTSeed;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NTSeedsHandler {

    private final NTSeedsDao ntSeedsDao;

    public NTSeedsHandler(NTSeedsDao ntSeedsDao) {
        this.ntSeedsDao = ntSeedsDao;
    }

    public void load(List<NTSeed> seeds) {
        ntSeedsDao.load(seeds);
    }
}
