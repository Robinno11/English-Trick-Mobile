package com.example.appmobileenglishtrick.data.repository

import com.example.appmobileenglishtrick.data.model.BaseQuestion
import com.example.appmobileenglishtrick.data.model.DifficultyLevel
import com.example.appmobileenglishtrick.data.model.ListeningQuestion
import com.example.appmobileenglishtrick.data.model.ReadingQuestion
import com.example.appmobileenglishtrick.data.model.SpeakingQuestion
import com.example.appmobileenglishtrick.data.model.TestCategory
import com.example.appmobileenglishtrick.data.model.WritingQuestion

object TestRepository {

    // ==========================================
    // READING QUESTIONS
    // ==========================================
    private val readingQuestions = listOf(
        // EASY
        ReadingQuestion(
            id = "read_easy_1",
            level = DifficultyLevel.EASY,
            passageTitle = "A Day in the Life of Sarah",
            passageText = "Sarah is a graphic designer living in London. She wakes up at 6:30 AM every weekday. Before starting work, she likes to jog in the park for thirty minutes. She arrives at her studio at 8:45 AM and drinks a cup of hot green tea. In the afternoon, she collaborates with her team on brand identity projects. Sarah believes that morning exercise keeps her creative and energetic throughout the workday.",
            questionText = "What does Sarah do before arriving at her studio?",
            options = listOf(
                "She drinks hot green tea at home",
                "She jogs in the park for thirty minutes",
                "She watches the morning news",
                "She designs logos in bed"
            ),
            correctIndex = 1,
            explanation = "The passage clearly states: 'Before starting work, she likes to jog in the park for thirty minutes.'"
        ),
        ReadingQuestion(
            id = "read_easy_2",
            level = DifficultyLevel.EASY,
            passageTitle = "Community Library Notice",
            passageText = "The Oakwood Community Library will be closed for renovation from Monday, October 5th until Friday, October 9th. All borrowed books due during this period will automatically have their return dates extended by one week without any overdue penalty. Members can still access our digital ebook collection 24/7 on the official website.",
            questionText = "What happens to books due between October 5th and October 9th?",
            options = listOf(
                "Members must pay an immediate fine",
                "They must be returned through the drop box",
                "Their due dates are extended by one week",
                "They are permanently donated to the library"
            ),
            correctIndex = 2,
            explanation = "The notice says: 'All borrowed books due during this period will automatically have their return dates extended by one week without any overdue penalty.'"
        ),
        ReadingQuestion(
            id = "read_easy_3",
            level = DifficultyLevel.EASY,
            passageTitle = "The Power of Regular Practice",
            passageText = "At English Trick, tutors always remind students that learning English is like playing a musical instrument. If you only practice once a month, you quickly forget what you learned. But if you speak and read for just ten minutes every day, your brain builds natural fluency. Our core belief is simple: English Trick No Speak No Trick!",
            questionText = "According to the passage, what is the best way to develop fluency?",
            options = listOf(
                "Studying 10 hours once a month",
                "Speaking and reading for 10 minutes every day",
                "Only memorizing grammar formulas without speaking",
                "Avoiding conversations until you are perfect"
            ),
            correctIndex = 1,
            explanation = "The text emphasizes practicing for just ten minutes every day builds natural fluency."
        ),

        // MEDIUM
        ReadingQuestion(
            id = "read_med_1",
            level = DifficultyLevel.MEDIUM,
            passageTitle = "Sustainable Urban Agriculture",
            passageText = "Urban rooftop gardens are transforming metropolitan landscapes across the globe. By converting barren commercial building rooftops into lush green zones, cities reduce the 'heat island effect', where concrete structures absorb and re-radiate solar heat. Moreover, these gardens provide hyperlocal produce, drastically cutting down transportation emissions and packaging waste. While the structural weight limit and irrigation logistics remain initial engineering hurdles, modern lightweight hydroponic systems offer highly viable solutions.",
            questionText = "What is identified as an initial challenge for urban rooftop gardens?",
            options = listOf(
                "Excessive demand for commercial produce",
                "Structural weight limitations and irrigation logistics",
                "Lack of sunlight on building roofs",
                "High transportation emissions"
            ),
            correctIndex = 1,
            explanation = "The passage mentions: 'While the structural weight limit and irrigation logistics remain initial engineering hurdles...'"
        ),
        ReadingQuestion(
            id = "read_med_2",
            level = DifficultyLevel.MEDIUM,
            passageTitle = "The Evolution of Workplace Autonomy",
            passageText = "The paradigm shift toward asynchronous remote work has necessitated a fundamental reassessment of productivity metrics. Traditional management models relied heavily on physical presence and observed working hours as proxies for employee dedication. Conversely, output-oriented frameworks focus exclusively on deliverables, project milestones, and objective impact. Empirical studies indicate that employees with greater schedule flexibility exhibit lower burnout rates and heightened problem-solving agility.",
            questionText = "How do output-oriented frameworks assess employee performance?",
            options = listOf(
                "By tracking the exact hours an employee stays seated",
                "By monitoring webcam feeds during office hours",
                "By evaluating deliverables, milestones, and objective impact",
                "By demanding daily in-person attendance"
            ),
            correctIndex = 2,
            explanation = "The text states that output-oriented frameworks focus exclusively on deliverables, project milestones, and objective impact."
        ),
        ReadingQuestion(
            id = "read_med_3",
            level = DifficultyLevel.MEDIUM,
            passageTitle = "Overcoming Linguistic Inhibitions",
            passageText = "Psycholinguistic research shows that communicative anxiety—often called the 'affective filter'—is the single greatest impediment to second language acquisition. When learners fear making grammatical errors, their cognitive bandwidth becomes saturated with self-monitoring rather than spontaneous speech generation. As championed by the English Trick philosophy, 'No Speak No Trick', embracing imperfections and vocalizing thoughts proactively lowers this psychological barrier.",
            questionText = "What does the term 'affective filter' refer to in the passage?",
            options = listOf(
                "A sophisticated microphone filter",
                "Communicative anxiety and psychological inhibition",
                "A formal grammar examination",
                "An algorithm for translating dialects"
            ),
            correctIndex = 1,
            explanation = "The text defines communicative anxiety as the 'affective filter' that acts as an impediment to second language acquisition."
        ),

        // HARD
        ReadingQuestion(
            id = "read_hard_1",
            level = DifficultyLevel.HARD,
            passageTitle = "Cognitive Plasticity & Polyglotism",
            passageText = "Neuroimaging evidence demonstrates that bilingualism induces neurostructural adaptations in the frontoparietal control network, which governs executive functions such as conflict monitoring, cognitive task switching, and inhibitory control. Lifelong navigation between two competing phonological and syntactic systems acts as continuous mental gymnastics, fortifying cognitive reserve. Intriguingly, longitudinal epidemiological analyses reveal that polyglots exhibit an average delay of four to five years in the clinical manifestation of degenerative neurocognitive pathologies compared to monolingual counterparts.",
            questionText = "What significant finding regarding polyglots does the epidemiological analysis reveal?",
            options = listOf(
                "They completely eliminate the biological risk of neurological diseases",
                "They exhibit a 4 to 5 year delay in the clinical manifestation of degenerative cognitive pathologies",
                "Their language switching abilities diminish executive brain functions",
                "They require higher caloric intake to maintain neural plasticity"
            ),
            correctIndex = 1,
            explanation = "The passage explicitly notes that polyglots exhibit an average delay of four to five years in clinical manifestation of degenerative neurocognitive pathologies."
        ),
        ReadingQuestion(
            id = "read_hard_2",
            level = DifficultyLevel.HARD,
            passageTitle = "The Dialectic of Algorithmic Governance",
            passageText = "As automated decision-making architectures permeate jurisprudence, credit adjudication, and social welfare allocation, institutional accountability faces unprecedented opacity. Machine learning models, particularly deep neural networks characterized by millions of non-linear parameter weights, often function as epistemic 'black boxes'. Consequently, affected citizens are deprived of meaningful procedural recourse, as the underlying rationale for adverse determinations cannot be rendered in intelligible natural language. Rectifying this systemic asymmetry demands mandatory algorithmic explainability audits prior to public deployment.",
            questionText = "Why are deep neural network systems described as 'epistemic black boxes'?",
            options = listOf(
                "Because they operate without any electrical power consumption",
                "Because their non-linear parameter weights make their internal rationale difficult to translate into intelligible human language",
                "Because their hardware is encased in black composite casings",
                "Because they can only process historical court data from the 19th century"
            ),
            correctIndex = 1,
            explanation = "The passage highlights that their millions of non-linear parameter weights obscure the underlying rationale, making them unable to be rendered in intelligible natural language."
        ),
        ReadingQuestion(
            id = "read_hard_3",
            level = DifficultyLevel.HARD,
            passageTitle = "Pragmatic Competence in English Diplomacy",
            passageText = "In high-stakes international negotiations, lexical precision is subordinate to pragmatic competence: the ability to interpret illocutionary intent, deference hierarchies, and culturally situated implicatures. A literalist interpretation of discourse frequently precipitates diplomatic gridlock. The pedagogy pioneered by English Trick posits that syntactic flawlessness without active communicative courage is vacuous—reaffirming the foundational dictum: English Trick No Speak No Trick.",
            questionText = "What is prioritized over lexical precision in international negotiations according to the text?",
            options = listOf(
                "Rote memorization of archaic grammar manuals",
                "Pragmatic competence: interpreting illocutionary intent, deference, and implicatures",
                "Rigid syntactic adherence regardless of context",
                "Maintaining absolute silence during tense dialogues"
            ),
            correctIndex = 1,
            explanation = "The first sentence indicates that lexical precision is subordinate to pragmatic competence, which encompasses understanding intent and cultural implicatures."
        )
    )

    // ==========================================
    // LISTENING QUESTIONS
    // ==========================================
    private val listeningQuestions = listOf(
        // EASY
        ListeningQuestion(
            id = "lis_easy_1",
            level = DifficultyLevel.EASY,
            audioTitle = "Ordering at the Downtown Cafe",
            audioSpeaker = "Barista & Customer",
            audioScript = "Barista: Hello, welcome to Downtown Cafe! What can I get started for you today? Customer: Hi! Could I please have a medium iced caramel latte with oat milk, and a warm blueberry muffin? Barista: Sure thing. That will be six dollars and fifty cents. Will that be for here or to go? Customer: To go, please. Here is ten dollars.",
            questionText = "What does the customer order besides the iced caramel latte?",
            options = listOf(
                "A chocolate croissant",
                "A warm blueberry muffin",
                "A slice of cheesecake",
                "A hot green tea"
            ),
            correctIndex = 1,
            explanation = "The customer requested 'a warm blueberry muffin' with their iced caramel latte."
        ),
        ListeningQuestion(
            id = "lis_easy_2",
            level = DifficultyLevel.EASY,
            audioTitle = "Central Station Platform Announcement",
            audioSpeaker = "Station Announcer",
            audioScript = "Attention all passengers for the 10:15 Express Train to Manchester. Please be advised that the departure platform has been changed from Platform 3 to Platform 7 due to track maintenance. Please proceed via the footbridge to Platform 7 immediately. All tickets remain valid.",
            questionText = "To which platform has the Manchester train been relocated?",
            options = listOf(
                "Platform 3",
                "Platform 5",
                "Platform 7",
                "Platform 10"
            ),
            correctIndex = 2,
            explanation = "The announcer stated the departure platform has been changed from Platform 3 to Platform 7."
        ),
        ListeningQuestion(
            id = "lis_easy_3",
            level = DifficultyLevel.EASY,
            audioTitle = "Welcome to English Trick",
            audioSpeaker = "Instructor David",
            audioScript = "Welcome students to your first session at English Trick. We are thrilled to guide you on this journey. Remember, you will not improve your speaking by just staring at textbooks. Always speak aloud in every lesson. As our golden rule says: English Trick No Speak No Trick!",
            questionText = "What is the golden rule mentioned by Instructor David?",
            options = listOf(
                "Read fast, speak slow",
                "English Trick No Speak No Trick",
                "Grammar first, speaking later",
                "Never make any mistakes"
            ),
            correctIndex = 1,
            explanation = "Instructor David states: 'As our golden rule says: English Trick No Speak No Trick!'"
        ),

        // MEDIUM
        ListeningQuestion(
            id = "lis_med_1",
            level = DifficultyLevel.MEDIUM,
            audioTitle = "University Course Orientation",
            audioSpeaker = "Professor Henderson",
            audioScript = "Good morning class. Welcome to Introduction to Macroeconomics. Before we delve into chapter one, please note that your mid-term paper is due on Friday, November 14th by midnight on the student portal. Late submissions will incur a five percent deduction per calendar day unless preceded by an official medical certificate. Additionally, my weekly office hours are held every Tuesday from 2 to 4 PM in Hall B, Room 302.",
            questionText = "What penalty applies to late mid-term paper submissions without a medical certificate?",
            options = listOf(
                "Automatic zero grade",
                "Five percent deduction per calendar day",
                "Ten points deducted per hour",
                "Mandatory oral re-examination"
            ),
            correctIndex = 1,
            explanation = "Professor Henderson explicitly announced that late submissions incur a 5% deduction per calendar day."
        ),
        ListeningQuestion(
            id = "lis_med_2",
            level = DifficultyLevel.MEDIUM,
            audioTitle = "Project Sprint Review Meeting",
            audioSpeaker = "Scrum Master Elena",
            audioScript = "Thanks everyone for joining our bi-weekly sprint review. Over the past two weeks, the mobile engineering team successfully resolved the audio latency bottleneck in our interactive modules. However, the user analytics dashboard is still experiencing synchronization delays when fetching historical quiz records. We will prioritize this bug fix in sprint forty-two, which kicks off next Monday.",
            questionText = "Which issue will the development team prioritize in sprint 42?",
            options = listOf(
                "Redesigning the logo and color palette",
                "Resolving audio latency bottlenecks",
                "Fixing synchronization delays in the user analytics dashboard",
                "Migrating server infrastructure to Europe"
            ),
            correctIndex = 2,
            explanation = "Elena notes that the analytics dashboard synchronization delays will be prioritized in sprint 42."
        ),
        ListeningQuestion(
            id = "lis_med_3",
            level = DifficultyLevel.MEDIUM,
            audioTitle = "The Philosophy of Fluency",
            audioSpeaker = "Coach Alex",
            audioScript = "Many language learners trap themselves in the illusion of perfection. They spend years accumulating grammar rules while remaining terrified of speaking. Here at English Trick, we invert that trap. We encourage you to talk, stammer, laugh, and try again. That is why our community motto stands strong: English Trick No Speak No Trick!",
            questionText = "What habit does Coach Alex advise learners to avoid?",
            options = listOf(
                "Joining language clubs",
                "Spending years accumulating rules while remaining terrified of speaking",
                "Speaking with an accent",
                "Reading English fiction books"
            ),
            correctIndex = 1,
            explanation = "Coach Alex warns against trapping oneself in the illusion of perfection by gathering rules without speaking."
        ),

        // HARD
        ListeningQuestion(
            id = "lis_hard_1",
            level = DifficultyLevel.HARD,
            audioTitle = "Symposium on Grid Modernization",
            audioSpeaker = "Keynote Speaker Dr. Vance",
            audioScript = "Ladies and gentlemen, transitioning our national electrical grid toward high-penetration renewables is no longer constrained primarily by photovoltaic efficiency, but rather by inertia management and transient stability. Conventional fossil generation inherently supplies rotational kinetic inertia through massive turbines, dampening frequency deviations. When we replace those synchronized turbines with inverter-based renewable generators, the grid becomes vulnerable to sudden voltage fluctuations. Consequently, deploying utility-scale synchronous condensers and grid-forming battery storage topologies must be codified into our baseline infrastructure mandate.",
            questionText = "What primary technical constraint does Dr. Vance identify regarding high-penetration renewables?",
            options = listOf(
                "Lack of global solar panel manufacturing capacity",
                "Inertia management and transient grid stability due to missing rotational kinetic energy",
                "Inability to transmit electricity over long distances",
                "Public unwillingness to adopt clean energy sources"
            ),
            correctIndex = 1,
            explanation = "Dr. Vance explained that replacing synchronized turbines removes rotational kinetic inertia, making inertia management and transient stability the main constraint."
        ),
        ListeningQuestion(
            id = "lis_hard_2",
            level = DifficultyLevel.HARD,
            audioTitle = "Bioethics Round Table on Genomic Editing",
            audioSpeaker = "Dr. Miriam Thorne",
            audioScript = "When scrutinizing somatic versus germline CRISPR editing, the moral calculus bifurcates sharply. Somatic modifications terminate with the treated individual, representing an evolutionary dead-end for that specific gene alteration. Germline interventions, by stark contrast, perpetuate modifications across successive generations, inextricably altering the human hereditary pool without the prospective consent of future progeny. The ethical imperative demands an indefinite moratorium on germline clinical trials until transgenerational biosafety is empirically validated.",
            questionText = "Why does Dr. Thorne argue for an indefinite moratorium specifically on germline editing?",
            options = listOf(
                "Because germline editing costs significantly more than somatic therapy",
                "Because it modifies hereditary genes across generations without the consent of future progeny",
                "Because somatic therapy is already completely forbidden by law",
                "Because laboratory equipment is prone to viral contamination"
            ),
            correctIndex = 1,
            explanation = "Dr. Thorne highlights that germline alterations perpetuate across successive generations without the prospective consent of unborn descendants."
        ),
        ListeningQuestion(
            id = "lis_hard_3",
            level = DifficultyLevel.HARD,
            audioTitle = "Advanced English Rhetoric Masterclass",
            audioSpeaker = "English Trick Lead Trainer",
            audioScript = "In public oratory, persuasion is seldom forged in syntactic complexity; it is crystallized in authentic cadence, purposeful pause, and unapologetic vocal resonance. Many students believe fluency is a cerebral trick of memory. Let us shatter that delusion once and for all: there are no shortcuts or passive tricks. Our foundational creed is immutable: English Trick No Speak No Trick. You must find your voice and articulate your reality.",
            questionText = "According to the masterclass, how is true persuasion forged in public oratory?",
            options = listOf(
                "Through arcane vocabulary and silent contemplation",
                "Through authentic cadence, purposeful pause, and vocal resonance",
                "By reciting memorized dictionaries verbatim",
                "By avoiding eye contact with the audience"
            ),
            correctIndex = 1,
            explanation = "The trainer emphasizes that persuasion is forged in authentic cadence, purposeful pauses, and vocal resonance."
        )
    )

    // ==========================================
    // SPEAKING QUESTIONS
    // ==========================================
    private val speakingQuestions = listOf(
        // EASY
        SpeakingQuestion(
            id = "spk_easy_1",
            level = DifficultyLevel.EASY,
            title = "Self Introduction & Goal",
            context = "Practice speaking your introductory goal with clarity and steady pacing.",
            targetSentence = "Hello, my name is Alex and I am practicing English with English Trick.",
            phoneticGuide = "həˈloʊ, maɪ neɪm ɪz ˈælɪks ænd aɪ æm ˈpræktɪsɪŋ ˈɪŋɡlɪʃ wɪð ˈɪŋɡlɪʃ trɪk.",
            tip = "Pronounce every consonant clearly and maintain a calm, steady rhythm."
        ),
        SpeakingQuestion(
            id = "spk_easy_2",
            level = DifficultyLevel.EASY,
            title = "Polite Assistance Request",
            context = "A standard daily request used at airports, hotels, or customer counters.",
            targetSentence = "Could you please help me find the nearest train station?",
            phoneticGuide = "kʊd ju pliːz hɛlp mi faɪnd ðə ˈnɪrɪst treɪn ˈsteɪʃən?",
            tip = "Use a gentle rising intonation at the end of the question."
        ),
        SpeakingQuestion(
            id = "spk_easy_3",
            level = DifficultyLevel.EASY,
            title = "Brand Slogan Challenge",
            context = "Vocalize the English Trick official slogan with energy and conviction!",
            targetSentence = "English Trick No Speak No Trick.",
            phoneticGuide = "ˈɪŋɡlɪʃ trɪk noʊ spiːk noʊ trɪk.",
            tip = "Deliver each word with strong emphasis: NO SPEAK, NO TRICK!"
        ),

        // MEDIUM
        SpeakingQuestion(
            id = "spk_med_1",
            level = DifficultyLevel.MEDIUM,
            title = "Expressing Personal Opinion",
            context = "Sharing your perspective in a professional work meeting or academic seminar.",
            targetSentence = "In my opinion, consistent daily practice is much more effective than studying once a week.",
            phoneticGuide = "ɪn maɪ əˈpɪnjən, kənˈsɪstənt ˈdeɪli ˈpræktɪs ɪz mʌtʃ mɔr ɪˈfɛktɪv ðæn ˈstʌdiɪŋ wʌns ə wik.",
            tip = "Group words into thought clusters: 'In my opinion' / 'consistent daily practice' / 'is much more effective'."
        ),
        SpeakingQuestion(
            id = "spk_med_2",
            level = DifficultyLevel.MEDIUM,
            title = "Balanced Argument on Technology",
            context = "Expressing nuance when discussing the benefits and drawbacks of modern tools.",
            targetSentence = "Although modern technology offers remarkable convenience, we must avoid excessive digital distractions.",
            phoneticGuide = "ɔlˈðoʊ ˈmɑdərn tɛkˈnɑlədʒi ˈɔfərz rɪˈmɑrkəbəl kənˈvinjəns, wi mʌst əˈvɔɪd ɪkˈsɛsɪv ˈdɪdʒətəl dɪˈstrækʃənz.",
            tip = "Pause briefly right after the comma to let your listener digest the contrast."
        ),
        SpeakingQuestion(
            id = "spk_med_3",
            level = DifficultyLevel.MEDIUM,
            title = "The Power of Active Speaking",
            context = "Reflecting on the English Trick philosophy during your progress review.",
            targetSentence = "I overcome my hesitation by remembering that English Trick means No Speak No Trick.",
            phoneticGuide = "aɪ ˌoʊvərˈkʌm maɪ ˌhɛzɪˈteɪʃən baɪ rɪˈmɛmbərɪŋ ðæt ˈɪŋɡlɪʃ trɪk miːnz noʊ spiːk noʊ trɪk.",
            tip = "Keep the transition smooth between 'remembering that' and the slogan."
        ),

        // HARD
        SpeakingQuestion(
            id = "spk_hard_1",
            level = DifficultyLevel.HARD,
            title = "Academic Discourse & Innovation",
            context = "Presenting formal findings at an international symposium or conference.",
            targetSentence = "Notwithstanding initial skepticism, innovative pedagogical methodologies have yielded unprecedented fluency outcomes.",
            phoneticGuide = "ˌnɑtwɪθˈstændɪŋ ɪˈnɪʃəl ˈskɛptɪˌsɪzəm, ɪˈnɑvətɪv ˌpɛdəˈɡɑdʒɪkəl ˌmɛθəˈdɑlədʒiz hæv ˈjildəd ʌnˈprɛsɪˌdɛntəd ˈfluənsi ˈaʊtˌkʌmz.",
            tip = "Pay close attention to syllable stress: NOT-with-standing, in-NO-vative, un-PRE-cedented."
        ),
        SpeakingQuestion(
            id = "spk_hard_2",
            level = DifficultyLevel.HARD,
            title = "Phonological Intonation & Precision",
            context = "Mastering complex tongue-twisting phonetic sequences with rapid articulation.",
            targetSentence = "Mastering subtle idiomatic nuances and phonological intonations demands rigorous dedication and continuous immersion.",
            phoneticGuide = "ˈmæstərɪŋ ˈsʌtəl ˌɪdiəˈmætɪk ˈnuɑnsəz ænd ˌfɑnəˈlɑdʒɪkəl ˌɪntəˈneɪʃənz dɪˈmændz ˈrɪɡərəs ˌdɛdəˈkeɪʃən ænd kənˈtɪnjuəs ɪˈmɜrʒən.",
            tip = "Do not pronounce the silent 'b' in subtle ('sut-tle') and articulate 'rigorous dedication' crisply."
        ),
        SpeakingQuestion(
            id = "spk_hard_3",
            level = DifficultyLevel.HARD,
            title = "The Ultimate English Trick Manifesto",
            context = "Delivering the core English Trick mission statement with maximum vocal authority.",
            targetSentence = "Genuine communicative mastery rejects passive complacency because at English Trick No Speak No Trick.",
            phoneticGuide = "ˈdʒɛnjuɪn kəˈmjunɪkətɪv ˈmæstəri rɪˈdʒɛkts ˈpæsɪv kəmˈpleɪsənsi bɪˈkɔz æt ˈɪŋɡlɪʃ trɪk noʊ spiːk noʊ trɪk.",
            tip = "Pronounce 'genuine communicative mastery' with full resonance and finish strongly on 'No Speak No Trick'!"
        )
    )

    // ==========================================
    // WRITING QUESTIONS
    // ==========================================
    private val writingQuestions = listOf(
        // EASY
        WritingQuestion(
            id = "wrt_easy_1",
            level = DifficultyLevel.EASY,
            topicTitle = "My Ideal Weekend Morning",
            prompt = "Write a short paragraph describing what you like to do on a Saturday morning. Mention what time you get up, what breakfast you enjoy, and one relaxing activity.",
            minWords = 30,
            suggestedKeywords = listOf("morning", "breakfast", "coffee", "relax", "enjoy"),
            modelAnswer = "On Saturday mornings, I usually wake up around 8:00 AM without an alarm clock. I enjoy making a warm breakfast of scrambled eggs and black coffee. Afterward, I sit in my backyard and read an inspiring English book for an hour. It is the most relaxing part of my week.",
            grammarTips = "Use the Simple Present tense for routines. Keep your sentences short and clear with basic connectors like 'afterward' and 'because'."
        ),
        WritingQuestion(
            id = "wrt_easy_2",
            level = DifficultyLevel.EASY,
            topicTitle = "Lunch Invitation Note",
            prompt = "Write a brief invitation message (30-50 words) to your colleague Alex. Invite him to have lunch at a new Italian restaurant near the office this Thursday.",
            minWords = 30,
            suggestedKeywords = listOf("Alex", "lunch", "Italian", "restaurant", "Thursday"),
            modelAnswer = "Hi Alex, hope you are having a productive week! A new Italian restaurant just opened across our office, and I would love to try their pasta. Would you like to join me for lunch this Thursday at 12:30 PM? Let me know if that works for you.",
            grammarTips = "Remember polite question forms: 'Would you like to join me...?' and friendly greetings."
        ),

        // MEDIUM
        WritingQuestion(
            id = "wrt_med_1",
            level = DifficultyLevel.MEDIUM,
            topicTitle = "Benefits of Learning a Second Language",
            prompt = "Write an opinion paragraph (60-100 words) discussing why mastering English opens new professional opportunities. Include two distinct advantages and conclude with a reference to active speaking.",
            minWords = 60,
            suggestedKeywords = listOf("opportunities", "global", "career", "confidence", "practice"),
            modelAnswer = "Mastering English is undoubtedly one of the most transformative professional investments. Firstly, it allows professionals to communicate seamlessly across international borders, unlocking multinational career opportunities and cross-cultural collaborations. Secondly, it provides direct access to cutting-edge research, industry publications, and specialized training programs. However, true fluency requires active vocal engagement rather than passive memorization. As English Trick reminds us, 'No Speak No Trick'—confidence flourishes only when you actively use your voice.",
            grammarTips = "Use structured transition words like 'Firstly', 'Secondly', and 'However'. Ensure subject-verb agreement across complex compound sentences."
        ),
        WritingQuestion(
            id = "wrt_med_2",
            level = DifficultyLevel.MEDIUM,
            topicTitle = "Professional Apology & Remedy Email",
            prompt = "Write a formal email (60-100 words) to a client apologizing for an unexpected delay in delivering their website redesign project. Explain the cause briefly and propose a concrete resolution.",
            minWords = 60,
            suggestedKeywords = listOf("apologize", "delay", "quality", "timeline", "resolution"),
            modelAnswer = "Dear Ms. Watson, I am writing to formally apologize for the unexpected delay regarding your website redesign launch. During our final security audit yesterday, our engineering team detected minor database vulnerabilities that required immediate remediation to protect your customer data. We have assigned two additional senior engineers to finalize the patch. We guarantee that your staging environment will be fully deployed by this Friday at 5:00 PM. Thank you for your continued patience.",
            grammarTips = "Adopt a professional, empathetic tone. Avoid passive blame and clearly state the revised timeline."
        ),

        // HARD
        WritingQuestion(
            id = "wrt_hard_1",
            level = DifficultyLevel.HARD,
            topicTitle = "Technology & Depth of Human Connection",
            prompt = "Compose an analytical essay paragraph (100-200 words) evaluating whether ubiquitous digital messaging enhances or diminishes the substantive depth of interpersonal human communication. Synthesize both perspectives before offering a reasoned conclusion.",
            minWords = 100,
            suggestedKeywords = listOf("hyper-connectivity", "superficial", "nuance", "empathy", "interpersonal"),
            modelAnswer = "The proliferation of instant digital messaging has engineered a paradox of hyper-connectivity coupled with emotional alienation. Proponents legitimately assert that asynchronous communication dismantles geographical barriers, enabling instantaneous collaboration and unprecedented global networking. However, when digitized text supplants face-to-face dialogue, critical communicative dimensions—such as micro-expressions, prosodic nuances, and visceral empathetic resonance—are systematically discarded. Consequently, interpersonal relationships risk devolving into superficial exchanges characterized by algorithmic brevity rather than intellectual depth. While digital channels offer remarkable utility, substantive intimacy and profound linguistic understanding demand the irreplaceable richness of authentic, spoken human voice.",
            grammarTips = "Employ advanced lexical cohesion, nominalizations, and subordinate clauses (e.g., 'While...', 'Consequently...', 'Proponents assert...'). Maintain formal academic register throughout."
        ),
        WritingQuestion(
            id = "wrt_hard_2",
            level = DifficultyLevel.HARD,
            topicTitle = "Strategic Policy Proposal: The 'No Speak No Trick' Pedagogy",
            prompt = "Draft an educational policy proposal (100-200 words) advocating for conversational immersion over passive rote testing in national foreign language curricula. Explicitly cite the motto 'English Trick No Speak No Trick' as your pedagogical rationale.",
            minWords = 100,
            suggestedKeywords = listOf("curriculum", "immersion", "fluency", "pedagogy", "No Speak No Trick"),
            modelAnswer = "Conventional English curricula have historically suffered from an over-reliance on passive rote grammar drills and mechanical multiple-choice examinations. While syntactic comprehension provides foundational architecture, it frequently breeds chronic communicative paralysis among students when thrust into spontaneous discourse. We propose a decisive institutional restructuring prioritizing conversational immersion and phonetic practice from primary levels onward. By grounding curriculum design in the empirical reality that language acquisition is inherently vocal, schools empower learners to internalize vocabulary dynamically. This strategy perfectly mirrors the core ethos of English Trick: 'No Speak No Trick'. Only through persistent, fearless articulation do students transform abstract grammatical theory into lifelong communicative empowerment.",
            grammarTips = "Structure your argument logically: Problem diagnosis -> Proposed intervention -> Rationale and concluding motto. Ensure varied sentence lengths and strong argumentative verbs."
        )
    )

    fun getQuestions(category: TestCategory, level: DifficultyLevel): List<BaseQuestion> {
        return when (category) {
            TestCategory.READING -> readingQuestions.filter { it.level == level }
            TestCategory.LISTENING -> listeningQuestions.filter { it.level == level }
            TestCategory.SPEAKING -> speakingQuestions.filter { it.level == level }
            TestCategory.WRITING -> writingQuestions.filter { it.level == level }
        }
    }

    fun getTotalQuestionCount(category: TestCategory, level: DifficultyLevel): Int {
        return getQuestions(category, level).size
    }
}
